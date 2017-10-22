package com.sanshan.service.consumer.handle;

import com.sanshan.dao.BlogVoteMapper;
import com.sanshan.dao.IpBlogVoteMapper;
import com.sanshan.pojo.entity.IpBlogVoteDO;
import com.sanshan.service.VoteService;
import com.sanshan.service.vo.VoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消费者
 * 从生产者线程中获得消息处理
 */
@Service
public class ConsumerHandler {

    @Autowired
    private IpBlogVoteMapper ipBlogVoteMapper;

    @Autowired
    private BlogVoteMapper blogVoteMapper;


    private ExecutorService pool = Executors.newCachedThreadPool();


    public void consumer(){
        voteConsumerProcess();
    }


    public void voteConsumerProcess(){
        pool.execute(()->{
            while (!VoteService.consumerQueue.isEmpty()) {
                VoteVo voteVo = VoteService.consumerQueue.poll();
                //TODO 将VoteVo存储到Mysql中
                IpBlogVoteDO ipBlogVoteDO = new IpBlogVoteDO(voteVo.getId(), voteVo.getBlogId(), voteVo.isVote());
                ipBlogVoteMapper.insert(ipBlogVoteDO);
                if (voteVo.isVote())
                    blogVoteMapper.incrFavours(voteVo.getBlogId());
                else
                    blogVoteMapper.incrTreads(voteVo.getBlogId());
            }
        });
    }
}
