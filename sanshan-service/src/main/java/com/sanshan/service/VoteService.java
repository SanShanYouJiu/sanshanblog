package com.sanshan.service;

import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.service.vo.VoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * 投票的相关变化
 */
@Service
public class VoteService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public static final String voteIpFavourZSetCachePrefix = "vote_favour_zset:ip:";
    public static final String voteIpTreadZSetCachePrefix = "vote_tread_zset:ip:";
    public static final String voteFavoursCachePrefix = "vote:favours:";
    public static final String voteTreadsCachePrefix = "vote:treads:";

    private ExecutorService pool = new ThreadPoolExecutor(0, 4,
            3, TimeUnit.MINUTES,
            new SynchronousQueue<Runnable>(), (r) -> {
        Thread t = new Thread();
        t.setName("vote-save-info-thread");
        return t;
    });

    //用于削峰 || 或者使用消息队列 还在考虑
    public static ConcurrentLinkedQueue<VoteVo> consumerQueue = new ConcurrentLinkedQueue<VoteVo>();


    //TODO 查询Blog的投票
    public void queryBlogInfo(Long blogId, ResponseMsgVO responseMsgVO) {
        Long blogFavours = Long.parseLong(redisTemplate.opsForValue().get(voteFavoursCachePrefix + blogId));
        Long blogTreads = Long.valueOf(redisTemplate.opsForValue().get(voteTreadsCachePrefix + blogId));

    }

    /**
     * 用户投票
     */
    public void userVote(Long blogId) {

    }

    /**
     * 游客投票
     * TODO: 加入redis 定时存储到Mysql上 Lazy-Store
     */
    public void anonymousVote(String ip, Long blogId, boolean vote, ResponseMsgVO responseMsgVO) {
        redisTemplate.opsForZSet().add(voteIpFavourZSetCachePrefix + ip, String.valueOf(blogId), blogId);
        redisTemplate.opsForZSet().add(voteIpTreadZSetCachePrefix + ip, String.valueOf(blogId), blogId);

        if (vote) {
            redisTemplate.opsForValue().increment(voteFavoursCachePrefix + blogId, 1);
        } else {
            redisTemplate.opsForValue().increment(voteTreadsCachePrefix + blogId, 1);
        }

        saveBlogVoteInfo(ip, blogId, vote);
        responseMsgVO.buildOK();
    }

    public void saveBlogVoteInfo(String ip, Long blogId, boolean vote) {
        pool.execute(() -> {
            consumerQueue.add(new VoteVo(ip, blogId, vote));
        });
    }

}
