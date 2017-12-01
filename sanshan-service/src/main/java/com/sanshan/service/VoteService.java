package com.sanshan.service;

import com.sanshan.dao.BlogVoteMapper;
import com.sanshan.dao.IpBlogVoteMapper;
import com.sanshan.pojo.dto.BlogVoteDTO;
import com.sanshan.pojo.dto.VoteDTO;
import com.sanshan.pojo.entity.BlogVoteDO;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.PosCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 投票的相关变化
 */
@Service
@Slf4j
public class VoteService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BlogVoteMapper blogVoteMapper;

    @Autowired
    private BlogIdGenerate blogIdGenerate;


    public static final String voteIpFavourZSetCachePrefix = "vote_favour_zset:ip:";
    public static final String voteIpTreadZSetCachePrefix = "vote_tread_zset:ip:";
    public static final String blogVoteFavoursCachePrefix = "blog_vote:favours:";
    public static final String blogVoteTreadsCachePrefix = "blog_vote:treads:";

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private ExecutorService pool = new ThreadPoolExecutor(0, 4,
            3, TimeUnit.MINUTES,
            new SynchronousQueue<Runnable>(), (r) -> {
        SecurityManager s = System.getSecurityManager();
        ThreadGroup group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        Thread t = new Thread(group, r, "vote-save-info-thread:" + poolNumber.incrementAndGet());
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    });

    //用于削峰 || 或者使用消息队列 还在考虑
    public static ConcurrentLinkedQueue<VoteDTO> consumerQueue = new ConcurrentLinkedQueue<VoteDTO>();


    /**
     * 查询Blog的投票
     * @param blogId
     * @param responseMsgVO
     */
    public void queryBlogInfo(Long blogId, ResponseMsgVO responseMsgVO) {
        Integer blogFavours = (Integer) redisTemplate.opsForValue().get(blogVoteFavoursCachePrefix + blogId);
        Integer blogTreads = (Integer) redisTemplate.opsForValue().get(blogVoteTreadsCachePrefix + blogId);
        if (Objects.isNull(blogFavours) && Objects.isNull(blogTreads)) {
            //首先查BlogIdGenerator的Id是否存在
            Boolean flag = blogIdGenerate.containsId(blogId);
            if (flag) {
                //查数据库
                BlogVoteDO voteDO = blogVoteMapper.queryVote(blogId);
                if (!Objects.isNull(voteDO)) {
                    blogFavours = voteDO.getFavours();
                    blogTreads = voteDO.getTreads();
                }
            } else {
                responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.NOT_FOUND, "该Id对应的博客不存在");
                return;
            }
        }
        if (Objects.isNull(blogFavours)) {
            blogFavours = 0;
        }
        if (Objects.isNull(blogTreads)) {
            blogTreads = 0;
        }
        BlogVoteDTO blogVoteDTO = new BlogVoteDTO(blogId, blogFavours, blogTreads);
        responseMsgVO.buildOKWithData(blogVoteDTO);
    }

    /**
     * 用户投票
     */
    public void userVote(Long blogId) {

    }

    /**
     * 游客投票
     * @param ip
     * @param blogId
     * @param vote
     * @param responseMsgVO
     */
    public void anonymousVote(String ip, Long blogId, boolean vote, ResponseMsgVO responseMsgVO) {
        //TODO 游客投票前判断是否已经投票过该博客 如果有则判断是否相反的操作
        if (vote) {
            redisTemplate.opsForZSet().add(voteIpFavourZSetCachePrefix + ip, blogId, blogId);
            redisTemplate.opsForValue().increment(blogVoteFavoursCachePrefix + blogId, 1);
        } else {
            redisTemplate.opsForZSet().add(voteIpTreadZSetCachePrefix + ip, blogId, blogId);
            redisTemplate.opsForValue().increment(blogVoteTreadsCachePrefix + blogId, 1);
        }

        saveBlogVoteInfo(ip, blogId, vote);
        responseMsgVO.buildOK();
    }

    /**
     * consumer线程定时对Vote信息进行处理
     */
    public void saveBlogVoteInfo(String ip, Long blogId, boolean vote) {
        pool.execute(() -> {
            if (log.isDebugEnabled()) {
                log.debug("将BlogId为:{}的投票为:{}组合而成的VoteDTO传输到Consumer线程中进行处理", blogId, vote);
            }
            consumerQueue.add(new VoteDTO(ip, blogId, vote));
        });
    }

}
