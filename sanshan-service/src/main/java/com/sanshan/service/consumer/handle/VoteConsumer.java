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
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
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


    protected void voteConsumerProcess() {
        pool.execute(() -> {
            while (!VoteService.consumerQueue.isEmpty()) {
                if (log.isDebugEnabled()){
                    log.debug("从VoteService中的consumer获得数据,即将存入Mysql中");
                }
                VoteDTO voteDTO = VoteService.consumerQueue.poll();
                //将VoteVo拆为IpBlogVote部分存储到Mysql中
                saveIpBlogVoteDO(voteDTO);
                if (voteDTO.getVote()){
                    incrFavour(voteDTO);
                }
                else {
                    incrTreads(voteDTO);
                }
            }
        });
    }

    /**
     *
     * @param voteDTO
     */
    private  void saveIpBlogVoteDO(VoteDTO voteDTO){
        //将VoteDTO存储到Mysql中
        IpBlogVoteDO ipBlogVoteDO = new IpBlogVoteDO(new Date(),new Date(),voteDTO.getId(), voteDTO.getBlogId(), voteDTO.getVote());
        ipBlogVoteMapper.insert(ipBlogVoteDO);
    }

    /**
     *
     * @param voteDTO
     */
    private void incrFavour(VoteDTO voteDTO){
        int rows = blogVoteMapper.incrFavours(voteDTO.getBlogId());
        if (rows == 0) {
            if (log.isDebugEnabled()){
                log.debug("blogVote表中不存在该行记录,改为插入一条新纪录");
            }
            BlogVoteDO blogVoteDO = new BlogVoteDO(new Date(),new Date(),voteDTO.getBlogId(), 1, 0);
            blogVoteMapper.insert(blogVoteDO);
        }
    }

    /**
     *
     * @param voteDTO
     */
    private void  incrTreads(VoteDTO voteDTO){
        int rows = blogVoteMapper.incrTreads(voteDTO.getBlogId());
        if (rows == 0) {
            if (log.isDebugEnabled()){
                log.debug("blogVote表中不存在该行记录,改为插入一条新纪录");
            }
            BlogVoteDO blogVoteDO = new BlogVoteDO(new Date(),new Date(),voteDTO.getBlogId(),0,1);
            blogVoteMapper.insert(blogVoteDO);
        }
    }



}
