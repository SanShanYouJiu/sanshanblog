package xyz.sanshan.main.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.dao.mongo.recommend.RecommendRepository;
import xyz.sanshan.main.pojo.dto.recommend.RecommendDTO;
import xyz.sanshan.main.pojo.entity.recommend.BlogRecommendDO;
import xyz.sanshan.main.pojo.entity.recommend.RecommendDO;
import xyz.sanshan.main.pojo.entity.recommend.UserRecommendDO;
import xyz.sanshan.main.service.convent.RecommendConvert;
import xyz.sanshan.main.service.recommend.BlogRecommendService;
import xyz.sanshan.main.service.recommend.UserRecommendService;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 目前是根据投票数计算的
 * 数据较小时可以这么干
 * 暂时3小时算一次
 */
@Service
@Slf4j
public class RecommendTask {

    @Autowired
    private RecommendRepository recommendRepository;

    @Autowired
    private BlogRecommendService blogRecommendService;

    @Autowired
    private UserRecommendService userRecommendService;

    private RecommendDTO recommendResult = new RecommendDTO();


    @PostConstruct
    public void init() {
        RecommendDO recommendDO;
        recommendDO = recommendRepository.findByNewestCreate();
        if (Objects.isNull(recommendDO)) {
            log.warn("mongo中没有推荐数据");
        } else {
            this.recommendResult = RecommendConvert.doToDto(recommendDO);
        }
    }


    /**
     * 推荐博客
     *
     * @param responseMsgVO
     * @return
     */
    public void recommendBlogs(ResponseMsgVO responseMsgVO) {
        responseMsgVO.buildOKWithData(recommendResult.getRecommendBlogs());
    }


    /**
     * 推荐用户
     *
     * @param responseMsgVO
     * @return
     */
    public void recommendUsers(ResponseMsgVO responseMsgVO) {
        responseMsgVO.buildOKWithData(recommendResult.getRecommendUsers());
    }


    /**
     * 生成推荐数据
     */
    @Scheduled(cron = "${recommend.quartz.expression:0 0 3 * * ?}")
    public void generateRecommedn() {
        RecommendDO recommendDO = new RecommendDO();
        List<UserRecommendDO> recommnedUsers= userRecommendService.generateUsers();
        List<BlogRecommendDO> recommnedBlogs= blogRecommendService.generateBlogs();
        recommendDO.setRecommendUsers(recommnedUsers);
        recommendDO.setRecommendBlogs(recommnedBlogs);
        recommendDO.setCreated(new Date());

        RecommendDO saveResultDO = recommendRepository.save(recommendDO);
        this.recommendResult = RecommendConvert.doToDto(saveResultDO);
    }

}
