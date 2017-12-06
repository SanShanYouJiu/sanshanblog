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
import java.util.concurrent.TimeUnit;

/**
 *
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

    private static final String BLOG_VOTE_NULL_EXIST_ID_PREFIX = "blog_vote:null_exist_id:";

    /**
     * 检查状态类
     */
    class InspectState {
        private Boolean advanceReturn = null;
        private Boolean needQueryDb = null;
        private Boolean dataExist = null;
        private Boolean cacheCurrent = null;

        private void blogExistNeedQueryDb() {
            this.dataExist = true;
            this.cacheCurrent = false;
            this.needQueryDb = true;
            this.advanceReturn = false;
        }

        private void blogNotExist() {
            this.dataExist = false;
            this.cacheCurrent = false;
            this.needQueryDb = false;
            this.advanceReturn = true;
        }

        private void cacheDbConsistency() {
            this.dataExist = true;
            this.cacheCurrent = true;
            this.needQueryDb = false;
            this.advanceReturn = true;
        }

        private void dbAlsoNotExistCacheCurrent() {
            this.dataExist = false;
            this.cacheCurrent = true;
            this.advanceReturn = false;
        }
    }


    /**
     * 查询Blog的投票
     *
     * @param blogId
     * @param responseMsgVO
     */
    public void queryBlogInfo(Long blogId, ResponseMsgVO responseMsgVO) {
        Integer blogFavours = (Integer) redisTemplate.opsForValue().get(VoteService.BLOG_VOTE_FAVOURS_PREFIX + blogId);
        Integer blogTreads = (Integer) redisTemplate.opsForValue().get(VoteService.BLOG_VOTE_THREADS_PREFIX + blogId);
        BlogVoteDTO blogVoteDTO = new BlogVoteDTO(blogId, blogFavours, blogTreads);
        //检查是否一致
        InspectState state = inspectDataConsistency(blogVoteDTO);
        //需要提前返回的情况
        if (state.advanceReturn) {
            if (state.cacheCurrent) {
                responseMsgVO.buildOKWithData(blogVoteDTO);
                return;
            } else if (!state.dataExist) {
                responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.NOT_FOUND, "该Id对应的博客不存在");
                return;
            }
        }
        if (!Objects.isNull(blogVoteDTO.getFavours())) {
            redisTemplate.opsForValue().set(VoteService.BLOG_VOTE_FAVOURS_PREFIX + blogId, blogVoteDTO.getFavours());
        }
        if (!Objects.isNull(blogVoteDTO.getTreads())) {
            redisTemplate.opsForValue().set(VoteService.BLOG_VOTE_THREADS_PREFIX + blogId, blogVoteDTO.getTreads());
        }

        responseMsgVO.buildOKWithData(blogVoteDTO);
    }

    /**
     * 检查缓存与数据库中的数目是否一致
     */
    private InspectState inspectDataConsistency(BlogVoteDTO blogVoteDTO) {
        //首先检查是否有必要查数据库
        InspectState state = preNeedQueryDbInspect(blogVoteDTO);
        if (state.needQueryDb) {
            //查数据库
            BlogVoteDO voteDO = blogVoteMapper.queryVote(blogVoteDTO.getBlogId());
            if (!Objects.isNull(voteDO)) {
                blogVoteDTO.setFavours(voteDO.getFavours());
                blogVoteDTO.setTreads(voteDO.getTreads());
                return state;
            } else {
                //数据库中的数据不存在(也就是数据库也没有投票
                state.dbAlsoNotExistCacheCurrent();
                //加入到缓存中
                redisTemplate.opsForValue().set(BLOG_VOTE_NULL_EXIST_ID_PREFIX + blogVoteDTO.getBlogId(), "vote data not exist", 60, TimeUnit.SECONDS);
                blogVoteDTO.setFavours(0);
                blogVoteDTO.setTreads(0);
                return state;
            }
        } else {
            //不需要查
            return state;
        }
    }


    /**
     * 是否查数据库之前的检查
     * 这里什么时候才去查数据库呢
     * 分为俩种情况 一种是数据都为空的(怀疑缓存失效
     * 一种是都为0的 怀疑是缓存在加的过程中出现失败的情况 但是这种情况一分钟只能查一次数据库
     *
     * @param blogVoteDTO
     * @return 需要则是true 否则是false
     */
    private InspectState preNeedQueryDbInspect(BlogVoteDTO blogVoteDTO) {
        InspectState state = new InspectState();
        if (Objects.isNull(blogVoteDTO.getFavours())
                && Objects.isNull(blogVoteDTO.getTreads())) {
            //首先查BlogIdGenerator的Id是否存在
            Boolean containsId = blogIdGenerate.containsId(blogVoteDTO.getBlogId());
            if (containsId) {
                state.blogExistNeedQueryDb();
                return state;
            } else {
                state.blogNotExist();
                return state;
            }
        } else {
            InspectState riskState = riskControl(blogVoteDTO);
            return riskState;
        }
    }

    /**
     * 进行风控 目前只设置这一种情况(都为0
     *
     * @param blogVoteDTO
     * @return
     */
    private InspectState riskControl(BlogVoteDTO blogVoteDTO) {
        InspectState state = new InspectState();
        //在俩者都为0的情况下(其实这是最正常的现象
        if (blogVoteDTO.getFavours().equals(0) && blogVoteDTO.getTreads().equals(0)) {
            //  每个缓存中投票数据为0对应博客只能60秒之内查询一次数据库 记录在Reids缓存中 超时自动消失 即可以查
            if (Objects.isNull(redisTemplate.opsForValue().get(BLOG_VOTE_NULL_EXIST_ID_PREFIX + blogVoteDTO.getBlogId()))) {
                state.blogExistNeedQueryDb();
            } else {
                state.cacheDbConsistency();
            }
            return state;
        } else {
            if (Objects.isNull(blogVoteDTO.getTreads())) {
                blogVoteDTO.setTreads(0);
            }
            if (Objects.isNull(blogVoteDTO.getFavours())){
                blogVoteDTO.setFavours(0);
            }
            state.cacheDbConsistency();
            return state;
        }
    }

}
