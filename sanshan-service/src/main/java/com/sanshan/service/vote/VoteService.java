package com.sanshan.service.vote;

import com.sanshan.pojo.dto.VoteDTO;
import com.sanshan.pojo.entity.IpBlogVoteDO;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.info.PosCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sanshan
 * www.85432173@qq.com
 * 投票的相关变化
 */
@Service
@Slf4j
public class VoteService {

    @Autowired
    private RedisTemplate redisTemplate;


    public static final String VOTE_IP_FAVOUR_PREFIX = "ip_vote:favour:";
    public static final String VOTE_IP_TREAD_PREFIX = "ip_vote:tread:";
    public static final String BLOG_VOTE_FAVOURS_PREFIX = "blog_vote:favours:";
    public static final String BLOG_VOTE_THREADS_PREFIX = "blog_vote:treads:";
    public static final String IP_VOTE_BLOG_ID_EXIST_PREFIX = "ip_vote:exist:";

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    private ExecutorService pool = new ThreadPoolExecutor(0, 4,
            3, TimeUnit.MINUTES,
            new SynchronousQueue<Runnable>(), (r) -> {
        SecurityManager s = System.getSecurityManager();
        ThreadGroup group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        Thread t = new Thread(group, r, "vote-save-info-thread:" + POOL_NUMBER.incrementAndGet());
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    });

    /**
     投票增加的处理队列
     */
    public static ConcurrentLinkedQueue<VoteDTO> voteAddConsumerQueue = new ConcurrentLinkedQueue<VoteDTO>();

    /**
     投票减少的处理队列
     */
    public static ConcurrentLinkedQueue<VoteDTO> voteDecrConsumerQueue = new ConcurrentLinkedQueue<VoteDTO>();




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
    public synchronized void anonymousVote(String ip, Long blogId, boolean vote, ResponseMsgVO responseMsgVO) {
        //首先检查是否可以投票
        if (inspectVote(ip, blogId, vote, responseMsgVO)) {
            //进行投票
            if (vote) {
                redisTemplate.opsForHash().put(VOTE_IP_FAVOUR_PREFIX + ip, blogId,true);
                redisTemplate.opsForHash().increment(BLOG_VOTE_FAVOURS_PREFIX , blogId, 1);
            } else {
                redisTemplate.opsForHash().put(VOTE_IP_TREAD_PREFIX + ip, blogId,true);
                redisTemplate.opsForHash().increment(BLOG_VOTE_THREADS_PREFIX , blogId, 1);
            }
            voteTransactionConsistentCheck(ip,blogId,vote);
            //加入到已投票域中
            redisTemplate.opsForHash().put(IP_VOTE_BLOG_ID_EXIST_PREFIX + ip, blogId, vote);
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
        Boolean flag = (Boolean) redisTemplate.opsForHash().get(IP_VOTE_BLOG_ID_EXIST_PREFIX + ip, blogId);
        if (!Objects.isNull(flag) && flag.equals(vote) ) {
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
    private   void voteTransactionConsistentCheck(String ip,Long blogId,Boolean vote) {
        setReverseVote(ip,blogId,vote);
        //具分为俩种情况 一种是没有投过的 一种是投过但是这次投了相反的票
        Boolean ipVote = (Boolean) redisTemplate.opsForHash().get(IP_VOTE_BLOG_ID_EXIST_PREFIX + ip, blogId);
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
            redisTemplate.opsForHash().delete(VOTE_IP_TREAD_PREFIX + ip, blogId);
        } else {
            redisTemplate.opsForHash().delete(VOTE_IP_FAVOUR_PREFIX + ip, blogId);
        }
    }

    /**
     *减少本次投票相反的一方数目减一
     * 这里的数据库操作定期执行（存入到相关的consumer中 否则打开数据库连接会太过频繁
     * @param blogId
     * @param vote
     */
    public void   decrReverseVotes(String ip,Long blogId,Boolean vote) {
        if (vote) {
            redisTemplate.opsForHash().increment(BLOG_VOTE_THREADS_PREFIX , blogId, -1);
        } else {
            redisTemplate.opsForHash().increment(BLOG_VOTE_FAVOURS_PREFIX , blogId, -1);
        }
        VoteDTO voteDTO = new VoteDTO();
        voteDecrConsumerQueue.add(voteDTO.decrVote(ip,blogId,vote));
    }


    /**
     * ip的投票信息
     * @param ip
     * @param responseMsgVO
     */
    public void ipVoteInfo(String ip, ResponseMsgVO responseMsgVO) {
        List<IpBlogVoteDO> ipVotes = new LinkedList<>();


    }

    /**
     * consumer线程定时对Vote信息进行处理
     * 这里是增加的投票信息
     * @param ip
     * @param blogId
     * @param vote
     */
    public void saveBlogVoteInfo(String ip, Long blogId, Boolean vote) {
        pool.execute(() -> {
            if (log.isDebugEnabled()) {
                log.debug("将BlogId为:{}的投票为:{}组合而成的VoteDTO传输到voteAddConsumerQueue线程中进行处理", blogId, vote);
            }
            VoteDTO voteDTO = new VoteDTO();
            voteAddConsumerQueue.add(voteDTO.addVote(ip,blogId,vote));
        });
    }


}
