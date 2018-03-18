package xyz.sanshan.main.service.vote;

import xyz.sanshan.main.pojo.dto.VoteDTO;
import xyz.sanshan.main.pojo.entity.IpBlogVoteDO;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.common.info.PosCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    public static final String VOTE_IP_FAVOUR_KEYS = "ip_vote:favour~keys";
    public static final String VOTE_IP_TREAD_PREFIX = "ip_vote:tread:";
    public static final String VOTE_IP_TREAD_KEYS = "ip_vote:tread~keys";
    public static final String IP_VOTE_BLOG_ID_EXIST_PREFIX = "ip_vote:exist:";
    public static final String IP_VOTE_BLOG_ID_EXIST_KEYS = "ip_vote:exist~keys";

    public static final String BLOG_VOTE_FAVOURS = "blog_vote:favours:";
    public static final String BLOG_VOTE_THREADS = "blog_vote:treads:";

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    private ExecutorService pool = new ThreadPoolExecutor(0, 8,
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

    public static ConcurrentLinkedQueue<VoteDTO> voteDeleteConsumerQueue = new ConcurrentLinkedQueue();




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
                redisTemplate.opsForSet().add(VOTE_IP_FAVOUR_KEYS, VOTE_IP_FAVOUR_PREFIX + ip);
                redisTemplate.opsForHash().increment(BLOG_VOTE_FAVOURS , blogId, 1);
                log.info("{}对id为{}的博客-赞",ip,blogId);
            } else {
                redisTemplate.opsForHash().put(VOTE_IP_TREAD_PREFIX + ip, blogId,true);
                redisTemplate.opsForSet().add(VOTE_IP_TREAD_KEYS, VOTE_IP_TREAD_PREFIX + ip);

                redisTemplate.opsForHash().increment(BLOG_VOTE_THREADS , blogId, 1);
                log.info("{}对id为{}的博客-踩",ip,blogId);
            }
            voteTransactionConsistentCheck(ip,blogId,vote);
            //加入到已投票域中
            redisTemplate.opsForHash().put(IP_VOTE_BLOG_ID_EXIST_PREFIX + ip, blogId, vote);
            redisTemplate.opsForSet().add(IP_VOTE_BLOG_ID_EXIST_KEYS, IP_VOTE_BLOG_ID_EXIST_PREFIX + ip);
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
        Boolean found = (Boolean) redisTemplate.opsForHash().get(IP_VOTE_BLOG_ID_EXIST_PREFIX + ip, blogId);
        if (!Objects.isNull(found) && found.equals(vote) ) {
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
            redisTemplate.opsForHash().increment(BLOG_VOTE_THREADS , blogId, -1);
        } else {
            redisTemplate.opsForHash().increment(BLOG_VOTE_FAVOURS , blogId, -1);
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

    /**
     * 删除博客时将投票数据也进行删除
     * @param blogId
     */
    public void deleteBlogVote(Long blogId) {
          Set<String> favourSet = redisTemplate.opsForSet().members(VOTE_IP_FAVOUR_KEYS);
          if (!Objects.isNull(favourSet)) {
              String[] favours = favourSet.toArray(new String[]{});
              for (int i = 0; i < favours.length; i++) {
                  redisTemplate.opsForHash().delete(favours[i], blogId);
              }
          }

          Set<String> treadSet = redisTemplate.opsForSet().members(VOTE_IP_TREAD_KEYS);
          if (!Objects.isNull(treadSet)) {
              String[] treads = treadSet.toArray(new String[]{});
              for (int i = 0; i < treads.length; i++) {
                  redisTemplate.opsForHash().delete(treads[i], blogId);
              }
          }

          Set<String> existSet = redisTemplate.opsForSet().members(IP_VOTE_BLOG_ID_EXIST_KEYS);
          if (!Objects.isNull(existSet)) {
              String[] exists = existSet.toArray(new String[]{});
              for (int i = 0; i < exists.length; i++) {
                  redisTemplate.opsForHash().delete(exists[i],blogId);
              }
          }

          redisTemplate.opsForHash().delete(BLOG_VOTE_FAVOURS,blogId);
          redisTemplate.opsForHash().delete(BLOG_VOTE_THREADS,blogId);
          VoteDTO voteDTO = new VoteDTO();
          voteDTO.setBlogId(blogId);
          log.debug("在缓存中删除id为{}的博客投票数据完成,删除请求进入到阻塞队列中 在一定时间内在数据库中进行删除",blogId);
          voteDeleteConsumerQueue.add(voteDTO);
    }



}
