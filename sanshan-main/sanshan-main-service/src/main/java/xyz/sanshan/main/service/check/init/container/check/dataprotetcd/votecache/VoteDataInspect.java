package xyz.sanshan.main.service.check.init.container.check.dataprotetcd.votecache;

import xyz.sanshan.main.dao.mybatis.BlogVoteMapper;
import xyz.sanshan.main.dao.mybatis.IpBlogVoteMapper;
import xyz.sanshan.main.pojo.entity.BlogVoteDO;
import xyz.sanshan.main.pojo.entity.IpBlogVoteDO;
import xyz.sanshan.main.service.vote.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 投票数据的可用性保证
 */
@Component
@Slf4j
public class VoteDataInspect {

    @Autowired
    private BlogVoteMapper blogVoteMapper;

    @Autowired
    private IpBlogVoteMapper ipBlogVoteMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 从数据库恢复数据
     */
    public void inspectDataConsistency() {
        Long initTime = System.currentTimeMillis();
        //进行点赞数据的事务完整性检查
        log.info("投票数据进行数据回滚");
        List<BlogVoteDO> blogVoteDOS = blogVoteMapper.selectAll();
        List<IpBlogVoteDO> ipBlogVoteDOS = ipBlogVoteMapper.selectAll();
        //开启事务
        SessionCallback sessionCallback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.multi();
                rollbackData(blogVoteDOS, ipBlogVoteDOS);
                return redisOperations.exec();
            }
        };
        //执行
        redisTemplate.execute(sessionCallback);
        log.info("从数据库中回滚投票数据的完成 耗时:{}ms", System.currentTimeMillis() - initTime);
    }

    /**
     * 数据回滚到缓存
     */
    private void rollbackData(List<BlogVoteDO> blogVoteDOS, List<IpBlogVoteDO> ipBlogVoteDOS) {
        blogVoteDOS.stream().forEach(blogVoteDO -> {
            redisTemplate.opsForHash().put(VoteService.BLOG_VOTE_FAVOURS, blogVoteDO.getBlogId(), blogVoteDO.getFavours());
            redisTemplate.opsForHash().put(VoteService.BLOG_VOTE_THREADS, blogVoteDO.getBlogId(), blogVoteDO.getTreads());
        });

        ipBlogVoteDOS.stream().forEach(ipBlogVoteDO -> {
            String ip = ipBlogVoteDO.getIp();
            Long blogId = ipBlogVoteDO.getBlogId();
            if (ipBlogVoteDO.getFavour()) {
                redisTemplate.opsForHash().put(VoteService.IP_VOTE_BLOG_ID_EXIST_PREFIX + ip, blogId, true);
                redisTemplate.opsForSet().add(VoteService.IP_VOTE_BLOG_ID_EXIST_KEYS,VoteService.IP_VOTE_BLOG_ID_EXIST_PREFIX + ip);

                redisTemplate.opsForHash().put(VoteService.VOTE_IP_FAVOUR_PREFIX + ip, blogId, true);
                redisTemplate.opsForSet().add(VoteService.VOTE_IP_FAVOUR_KEYS,VoteService.VOTE_IP_FAVOUR_PREFIX + ip);
            } else {

                redisTemplate.opsForHash().put(VoteService.IP_VOTE_BLOG_ID_EXIST_PREFIX + ip, blogId, false);
                redisTemplate.opsForSet().add(VoteService.IP_VOTE_BLOG_ID_EXIST_KEYS, VoteService.IP_VOTE_BLOG_ID_EXIST_PREFIX + ip);

                redisTemplate.opsForHash().put(VoteService.VOTE_IP_TREAD_PREFIX + ip, blogId, true);
                redisTemplate.opsForSet().add(VoteService.VOTE_IP_TREAD_KEYS, VoteService.VOTE_IP_TREAD_PREFIX + ip);

            }
        });
    }

}
