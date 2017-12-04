package com.sanshan.service.vote;

import com.sanshan.dao.BlogVoteMapper;
import com.sanshan.pojo.dto.BlogVoteDTO;
import com.sanshan.pojo.entity.BlogVoteDO;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.PosCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 */
@Service
@Slf4j
public class BlogVoteInfoService {


    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private BlogIdGenerate blogIdGenerate;

    @Autowired
    private BlogVoteMapper blogVoteMapper;

    /**
     * 查询Blog的投票
     * @param blogId
     * @param responseMsgVO
     */
    public void queryBlogInfo(Long blogId, ResponseMsgVO responseMsgVO) {
        Integer blogFavours = (Integer) redisTemplate.opsForValue().get(VoteService.BLOG_VOTE_FAVOURS_PREFIX + blogId);
        Integer blogTreads = (Integer) redisTemplate.opsForValue().get(VoteService.BLOG_VOTE_THREADS_PREFIX + blogId);
        //TODO 检查是否真的强一致 并且将这一大段拆为一个方法
        if (Objects.isNull(blogFavours) && Objects.isNull(blogTreads)) {
            //首先查BlogIdGenerator的Id是否存在
            Boolean flag = blogIdGenerate.containsId(blogId);
            if (flag) {
                //查数据库
                BlogVoteDO voteDO = blogVoteMapper.queryVote(blogId);
                if (!Objects.isNull(voteDO)) {
                    blogFavours = voteDO.getFavours();
                    blogTreads = voteDO.getTreads();
                }
            } else {
                responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.NOT_FOUND, "该Id对应的博客不存在");
                return;
            }
        }
        if (Objects.isNull(blogFavours)) {
            blogFavours = 0;
        } else {
            redisTemplate.opsForValue().set(VoteService.BLOG_VOTE_FAVOURS_PREFIX + blogId, blogFavours);
        }
        if (Objects.isNull(blogTreads)) {
            blogTreads = 0;
        } else {
            redisTemplate.opsForValue().set(VoteService.BLOG_VOTE_THREADS_PREFIX + blogId, blogTreads);
        }
        BlogVoteDTO blogVoteDTO = new BlogVoteDTO(blogId, blogFavours, blogTreads);
        responseMsgVO.buildOKWithData(blogVoteDTO);
    }

}
