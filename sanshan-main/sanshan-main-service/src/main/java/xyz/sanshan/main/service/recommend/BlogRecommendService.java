package xyz.sanshan.main.service.recommend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sanshan.common.info.EditorTypeEnum;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.dao.mongo.recommend.BlogRecommendRepository;
import xyz.sanshan.main.pojo.dto.BlogVoteDTO;
import xyz.sanshan.main.pojo.entity.recommend.BlogRecommendDO;
import xyz.sanshan.main.service.BlogService;
import xyz.sanshan.main.service.convent.BlogRecommendConvert;
import xyz.sanshan.main.service.editor.BlogIdGenerate;
import xyz.sanshan.main.service.vo.BlogVO;
import xyz.sanshan.main.service.vote.BlogVoteInfoService;

import java.util.*;

@Service
@Slf4j
public class BlogRecommendService {

    @Autowired
    private BlogVoteInfoService blogVoteInfoService;

    @Autowired
    private BlogIdGenerate blogIdGenerate;

    @Autowired
    private BlogRecommendRepository blogRecommendRepository;

    @Autowired
    private BlogService blogService;


    private Map<Long, Double> recommendRateMap = new HashMap<>();

    private final Integer generateBlogs = 5;


    /**
     * 生成推荐博客
     *
     * @return
     */
    public List<BlogRecommendDO> generateBlogs() {
        log.info("生成一次 推荐博客数据");

        Map<Long, EditorTypeEnum> idMap = blogIdGenerate.getIdCopy();

        //放到这里自动排序
        Set<BlogVoteDTO> blogVoteDTOS = new TreeSet<BlogVoteDTO>((b1, b2) -> {
            return recommendCalculate(b1, b2);
        });

        //获取点赞情况
        for (Map.Entry<Long, EditorTypeEnum> entry : idMap.entrySet()) {
            Long blogId = entry.getKey();
            ResponseMsgVO responseMsgVO = new ResponseMsgVO();
            blogVoteInfoService.queryBlogInfo(blogId, responseMsgVO);
            if (responseMsgVO.getStatus().equals(PosCodeEnum.OK.getStatus())) {
                BlogVoteDTO blogVoteDTO = (BlogVoteDTO) responseMsgVO.getData();
                blogVoteDTOS.add(blogVoteDTO);
            } else {
                continue;
            }
        }

        List<BlogRecommendDO> result = buildAndSaveResult(blogVoteDTOS);
        return result;
    }


    /**
     * 组装并存储计算结果
     *
     * @param blogVoteDTOS
     * @return
     */
    private List<BlogRecommendDO> buildAndSaveResult(Set<BlogVoteDTO> blogVoteDTOS) {
        //返回给前端的list
        List<BlogRecommendDO> recommendBlogs = new LinkedList<>();


        //放到这里自动排序
        Iterator<BlogVoteDTO> it = blogVoteDTOS.iterator();
        int i = 0;

        //存储结果
        while (it.hasNext()) {
            Long blogId = it.next().getBlogId();
            BlogVO blog = blogService.getBlog(blogId);
            BlogRecommendDO blogRecommendDO = BlogRecommendConvert.voToDto(blog);
            //添加Recommend相关属性
            blogRecommendDO.setRecommendRate(recommendRateMap.get(blogId));
            blogRecommendDO.setCreated(new Date());
            //如果有就更新
            blogRecommendRepository.save(blogRecommendDO);
            //组装需要返回给前端的结果
            if (i < generateBlogs) {
                recommendBlogs.add(blogRecommendDO);
            }
            i++;
        }
        return recommendBlogs;
    }

    /**
     * 推荐算法  算法简陋
     *
     * @param b1
     * @param b2
     * @return
     */
    private int recommendCalculate(BlogVoteDTO b1, BlogVoteDTO b2) {

        int b1Favours = b1.getFavours();
        int b1Treads = b1.getTreads();
        int b1Votes = b1Favours + b1Treads;
        int b2Favours = b2.getFavours();
        int b2Treads = b2.getTreads();
        int b2Votes = b2Favours + b2Treads;


        //精品比例
        double b1Rate;
        b1Rate = excellentRate(b1Favours, b1Treads);
        double b2Rate;
        b2Rate = excellentRate(b2Favours, b2Treads);

        //推荐比例
        double b1RecommendRate;
        b1RecommendRate = recommendRate(b1Votes, b1Rate);
        double b2RecommendRate;
        b2RecommendRate = recommendRate(b2Votes, b2Rate);

        //存一下
        recommendRateMap.put(b1.getBlogId(), b1RecommendRate);
        recommendRateMap.put(b2.getBlogId(), b2RecommendRate);

        //不设置0 因为每个值都是有意义的
        if (b1RecommendRate > b2RecommendRate) {
            return -1;
        } else if (b1RecommendRate == b2RecommendRate) {
            //相同的参数列在后面吧
            return 1;
        } else {
            return 1;
        }
    }

    /**
     * 精品比例
     * 比例越大越好
     *
     * @param favours
     * @param treads
     * @return
     */
    private double excellentRate(int favours, int treads) {
        double rate = 1;
        //防止 /zero 异常
        if (favours == 0 && treads != 0) {
            treads+=1;
            rate = (double)1 / (double)treads;
        } else if (favours != 0 && treads == 0) {
            rate = favours;
        } else if (favours == 0 & treads == 0) {
            rate = 0.8;
        } else {
            treads += 1;
            rate = (double)favours / (double)treads;
        }
        return rate;
    }


    /**
     * 推荐比例
     * 比例越大越好
     * <p>
     * 非常简陋
     *
     * @param votes         投票总数
     * @param excellentRate 精品比例
     * @return
     */
    private double recommendRate(Integer votes, Double excellentRate) {
        double recommendRate;
        //0的情况
        if (votes==0){
            votes=1;
        }
        recommendRate = (excellentRate * excellentRate) * votes;
        return recommendRate;
    }


}
