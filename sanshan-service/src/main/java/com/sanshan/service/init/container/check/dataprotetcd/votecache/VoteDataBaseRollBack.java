package com.sanshan.service.init.container.check.dataprotetcd.votecache;

import com.sanshan.dao.BlogVoteMapper;
import com.sanshan.dao.IpBlogVoteMapper;
import com.sanshan.pojo.entity.BlogVoteDO;
import com.sanshan.pojo.entity.IpBlogVoteDO;
import com.sanshan.service.vote.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 投票数据的可用性保证
 */
@Component
@Slf4j
public class VoteDataBaseRollBack {

    @Autowired
    private BlogVoteMapper blogVoteMapper;

    @Autowired
    private IpBlogVoteMapper ipBlogVoteMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     检查是否需要从数据库恢复数据
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void inspectDataConsistency() {
        Long initTime = System.currentTimeMillis();
        //进行点赞数据的事务完整性检查
        log.info("投票数据进行事物一致性检查");
        List<BlogVoteDO> blogVoteDOS = blogVoteMapper.selectAll();
        List<IpBlogVoteDO> ipBlogVoteDOS = ipBlogVoteMapper.selectAll();
        rollbackData(blogVoteDOS, ipBlogVoteDOS);
        log.info("从数据库中检查投票数据的一致性完成 耗时:{}ms", System.currentTimeMillis() - initTime);
    }

    /**
     数据回滚到缓存
     */
    public void rollbackData(List<BlogVoteDO> blogVoteDOS, List<IpBlogVoteDO> ipBlogVoteDOS) {

        blogVoteDOS.stream().forEach(blogVoteDO -> {
            redisTemplate.opsForValue().set(VoteService.BLOG_VOTE_FAVOURS_PREFIX + blogVoteDO.getBlogId(), blogVoteDO.getFavours());
            redisTemplate.opsForValue().set(VoteService.BLOG_VOTE_THREADS_PREFIX + blogVoteDO.getBlogId(), blogVoteDO.getTreads());
        });

        ipBlogVoteDOS.stream().forEach(ipBlogVoteDO -> {
            String ip = ipBlogVoteDO.getIp();
            Long blogId = ipBlogVoteDO.getBlogId();
            if (ipBlogVoteDO.getFavour()) {
                redisTemplate.opsForHash().put(VoteService.IP_VOTE_BLOG_ID_EXIST_PREFIX + ip, blogId, true);
                redisTemplate.opsForHash().put(VoteService.VOTE_IP_FAVOUR_PREFIX+ip, blogId, true);
            }else {
                redisTemplate.opsForHash().put(VoteService.IP_VOTE_BLOG_ID_EXIST_PREFIX+ip,blogId,false);
                redisTemplate.opsForHash().put(VoteService.VOTE_IP_TREAD_PREFIX + ip, blogId, true);
            }
        });
    }

}
