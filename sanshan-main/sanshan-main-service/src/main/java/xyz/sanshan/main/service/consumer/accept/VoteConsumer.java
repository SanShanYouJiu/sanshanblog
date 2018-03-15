package xyz.sanshan.main.service.consumer.accept;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import xyz.sanshan.main.dao.mybatis.BlogVoteMapper;
import xyz.sanshan.main.dao.mybatis.IpBlogVoteMapper;
import xyz.sanshan.main.pojo.dto.VoteDTO;
import xyz.sanshan.main.pojo.entity.BlogVoteDO;
import xyz.sanshan.main.pojo.entity.IpBlogVoteDO;
import xyz.sanshan.main.service.vote.VoteService;

import java.util.Date;

/**
 */
@Service
@Slf4j
public class VoteConsumer {

    @Autowired
    private IpBlogVoteMapper ipBlogVoteMapper;

    @Autowired
    private BlogVoteMapper blogVoteMapper;


    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void voteConsumerProcess() {
        if (!VoteService.voteAddConsumerQueue.isEmpty() || !VoteService.voteDecrConsumerQueue.isEmpty()||!VoteService.voteDeleteConsumerQueue.isEmpty()) {
            if (log.isDebugEnabled()){
                log.debug("lazy投票数据到数据库中");
            }
              voteExecute();
        }
    }

    private void voteExecute(){
        voteAdd();
        voteDecr();
        voteDelete();
    }

    /**
     * 对Vote增加的consumer进行处理
     */
    private  void  voteAdd(){
        while (!VoteService.voteAddConsumerQueue.isEmpty()) {
            if (log.isDebugEnabled()){
                log.debug("从VoteService中的voteAddConsumerQueue获得数据,即将存入Mysql中");
            }
            VoteDTO voteDTO = VoteService.voteAddConsumerQueue.poll();
            //将VoteVo拆为IpBlogVote部分存储到Mysql中
            saveIpBlogVoteDO(voteDTO);
            if (voteDTO.getVote()){
                incrFavour(voteDTO);
            }
            else {
                incrTreads(voteDTO);
            }
        }
    }

    /**
     * 对Vote的减少的Consumer进行处理
     */
    private  void  voteDecr(){
        while (!VoteService.voteDecrConsumerQueue.isEmpty()) {
            if (log.isDebugEnabled()){
                log.debug("从VoteService中的voteDecrConsumerQueue获得数据,即将在Mysql执行");
            }
            VoteDTO voteDTO = VoteService.voteDecrConsumerQueue.poll();
            if (voteDTO.getVote()){
                blogVoteMapper.decrTreads(voteDTO.getBlogId());
                ipBlogVoteMapper.deleteVoteTreadByBlogId(voteDTO.getIp(), voteDTO.getBlogId());
            }else {
                blogVoteMapper.decrFavours(voteDTO.getBlogId());
                ipBlogVoteMapper.deleteVoteFavourByBlogId(voteDTO.getIp(), voteDTO.getBlogId());
            }
        }
    }

    /**
     * 删除相关投票数据
     */
    private void voteDelete() {
        while (!VoteService.voteDeleteConsumerQueue.isEmpty()) {
            VoteDTO voteDTO = VoteService.voteDeleteConsumerQueue.poll();
            Long blogId = voteDTO.getBlogId();
            if (log.isDebugEnabled()){
                log.debug("删除id为{}的投票数据",blogId);
            }
            ipBlogVoteMapper.deleteByBlogId(blogId);
            blogVoteMapper.deleteByBlogId(blogId);
        }
    }

    /**
     *
     * @param voteDTO
     */
    private  void saveIpBlogVoteDO(VoteDTO voteDTO){
        //将VoteDTO存储到Mysql中
        IpBlogVoteDO ipBlogVoteDO = new IpBlogVoteDO(new Date(),new Date(),voteDTO.getIp(), voteDTO.getBlogId(), voteDTO.getVote());
        if (log.isDebugEnabled()){
            log.debug("增加id为{}的投票数据",voteDTO.getBlogId());
        }
        ipBlogVoteMapper.insert(ipBlogVoteDO);
    }

    /**
     *
     * @param voteDTO
     */
    private void incrFavour(VoteDTO voteDTO){
        if (log.isDebugEnabled()){
            log.debug("在blogVote表中对id为{}的行增加赞数",voteDTO.getBlogId());
        }
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
        if (log.isDebugEnabled()){
            log.debug("在blogVote表中对id为{}的行增加踩数",voteDTO.getBlogId());
        }
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
