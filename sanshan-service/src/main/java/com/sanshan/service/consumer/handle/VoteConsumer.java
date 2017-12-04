package com.sanshan.service.consumer.handle;

import com.sanshan.dao.BlogVoteMapper;
import com.sanshan.dao.IpBlogVoteMapper;
import com.sanshan.pojo.dto.VoteDTO;
import com.sanshan.pojo.entity.BlogVoteDO;
import com.sanshan.pojo.entity.IpBlogVoteDO;
import com.sanshan.service.vote.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW)
public class VoteConsumer {

    @Autowired
    private IpBlogVoteMapper ipBlogVoteMapper;

    @Autowired
    private BlogVoteMapper blogVoteMapper;


    private ExecutorService pool = new ThreadPoolExecutor(0,4,3, TimeUnit.MINUTES,new SynchronousQueue<Runnable>(),(r)->{
        Thread t = new Thread(r);
        t.setName("vote-consumer-thread");
        return t;
    });

    //TODO 太长了  必须拆
    protected void voteConsumerProcess() {
        pool.execute(() -> {
            while (!VoteService.consumerQueue.isEmpty()) {
                if (log.isDebugEnabled()){
                    log.debug("从VoteService中的consumer获得数据,即将存入Mysql中");
                }
                VoteDTO voteVo = VoteService.consumerQueue.poll();
                //将VoteVo存储到Mysql中
                IpBlogVoteDO ipBlogVoteDO = new IpBlogVoteDO(new Date(),new Date(),voteVo.getId(), voteVo.getBlogId(), voteVo.isVote());
                ipBlogVoteMapper.insert(ipBlogVoteDO);
                if (voteVo.isVote()){
                    int rows = blogVoteMapper.incrFavours(voteVo.getBlogId());
                    if (rows == 0) {
                        if (log.isDebugEnabled()){
                            log.debug("blogVote表中不存在该行记录,改为插入一条新纪录");
                        }
                        BlogVoteDO blogVoteDO = new BlogVoteDO(new Date(),new Date(),voteVo.getBlogId(), 1, 0);
                        blogVoteMapper.insert(blogVoteDO);
                    }
                }
                else {
                    int rows = blogVoteMapper.incrTreads(voteVo.getBlogId());
                    if (rows == 0) {
                        if (log.isDebugEnabled()){
                            log.debug("blogVote表中不存在该行记录,改为插入一条新纪录");
                        }
                        BlogVoteDO blogVoteDO = new BlogVoteDO(new Date(),new Date(),voteVo.getBlogId(),0,1);
                        blogVoteMapper.insert(blogVoteDO);
                    }
                }
            }
        });
    }


}
