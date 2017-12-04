package com.sanshan.service.vote;

import com.sanshan.dao.BlogVoteMapper;
import com.sanshan.dao.IpBlogVoteMapper;
import com.sanshan.pojo.dto.VoteDTO;
import com.sanshan.pojo.entity.IpBlogVoteDO;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.info.PosCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
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
    private IpBlogVoteMapper ipBlogVoteMapper;

    @Autowired
    private BlogVoteMapper blogVoteMapper;


    public static final String voteIpFavourPrefix = "ip_vote:favour:";
    public static final String voteIpTreadPrefix = "ip_vote:tread:";
    public static final String blogVoteFavoursPrefix = "blog_vote:favours:";
    public static final String blogVoteTreadsPrefix = "blog_vote:treads:";
    public static final String IpVoteBlogIdExistPrefix = "ip_vote:exist:";

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
     * 用户投票
     * @param blogId
     */
    public void userVote(Long blogId) {

    }

    /**
     * 游客投票
     *
     * @param ip
     * @param blogId
     * @param vote
     * @param responseMsgVO
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW)
    public void anonymousVote(String ip, Long blogId, boolean vote, ResponseMsgVO responseMsgVO) {
        //首先检查是否可以投票
        if (inspectVote(ip, blogId, vote, responseMsgVO)) {
            //进行投票
            if (vote) {
                redisTemplate.opsForHash().put(voteIpFavourPrefix + ip, blogId,true);
                redisTemplate.opsForValue().increment(blogVoteFavoursPrefix + blogId, 1);
            } else {
                redisTemplate.opsForHash().put(voteIpTreadPrefix + ip, blogId,true);
                redisTemplate.opsForValue().increment(blogVoteTreadsPrefix + blogId, 1);
            }
            voteTransactionConsistentCheck(ip,blogId,vote);
            //加入到已投票域中
            redisTemplate.opsForHash().put(IpVoteBlogIdExistPrefix + ip, blogId, vote);
            saveBlogVoteInfo(ip, blogId, vote);
            responseMsgVO.buildOK();
        }
    }

    /**
     * 游客投票前判断是否已经投票过该博客 如果有则判断是否相反的操作
     *
     * @param ip
     * @param blogId
     * @param responseMsgVO
     * @return
     */
    private Boolean inspectVote(String ip, Long blogId, Boolean vote, ResponseMsgVO responseMsgVO) {
        Boolean flag = (Boolean) redisTemplate.opsForHash().get(IpVoteBlogIdExistPrefix + ip, blogId);
        if (!Objects.isNull(flag) && flag == vote) {
            responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.URL_ERROR, "已对Id为:" + blogId + "的博客投票过");
            return false;
        } else {
            return true;
        }
    }


    /**
     * 投票事务一致性检查
     * @return
     */
    private void voteTransactionConsistentCheck(String ip,Long blogId,Boolean vote) {
        setReverseVote(ip,blogId,vote);
        //具分为俩种情况 一种是没有投过的 一种是投过但是这次投了相反的票
        Boolean ipVote = (Boolean) redisTemplate.opsForHash().get(IpVoteBlogIdExistPrefix + ip, blogId);
        if (Objects.isNull(ipVote)) {
        }else {
            decrReverseVotes(ip,blogId, vote);
        }
    }


    /**
     * 设置本次投票相反的一方为未投状态
     * @param ip
     * @param blogId
     * @param vote
     */
    private void setReverseVote(String ip,Long blogId,Boolean vote){
        if (vote) {
            redisTemplate.opsForHash().put(voteIpTreadPrefix + ip, blogId, false);
        } else {
            redisTemplate.opsForHash().put(voteIpFavourPrefix + ip, blogId, false);
        }
    }

    /**
     *减少本次投票相反的一方数目减一
     * @param blogId
     * @param vote
     */
    public void   decrReverseVotes(String ip,Long blogId,Boolean vote) {
        if (vote) {
            redisTemplate.opsForValue().increment(blogVoteTreadsPrefix + blogId, -1);
            blogVoteMapper.decrTreads(blogId);
            ipBlogVoteMapper.deleteVoteTreadByBlogId(ip, blogId);
        } else {
            redisTemplate.opsForValue().increment(blogVoteFavoursPrefix + blogId, -1);
            blogVoteMapper.decrFavours(blogId);
            ipBlogVoteMapper.deleteVoteFavourByBlogId(ip, blogId);

        }
    }


    /**
     * ip的投票信息
     * @param ip
     * @param responseMsgVO
     */
    public void ipVoteInfo(String ip, ResponseMsgVO responseMsgVO) {
        List<IpBlogVoteDO> ipVotes = new LinkedList<>();

        //查数据库
        IpBlogVoteDO ipBlogVoteDO = ipBlogVoteMapper.queryByIp(ip);

    }

    /**
     * consumer线程定时对Vote信息进行处理
     *
     * @param ip
     * @param blogId
     * @param vote
     */
    public void saveBlogVoteInfo(String ip, Long blogId, Boolean vote) {
        pool.execute(() -> {
            if (log.isDebugEnabled()) {
                log.debug("将BlogId为:{}的投票为:{}组合而成的VoteDTO传输到Consumer线程中进行处理", blogId, vote);
            }
            consumerQueue.add(new VoteDTO(ip, blogId, vote));
        });
    }


}
