package xyz.sanshan.main.service.recommend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sanshan.main.service.editor.BlogIdGenerate;
import xyz.sanshan.common.info.EditorTypeEnum;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.pojo.dto.BaseBlogDTO;
import xyz.sanshan.main.pojo.dto.BlogVoteDTO;
import xyz.sanshan.main.pojo.dto.MarkDownBlogDTO;
import xyz.sanshan.main.pojo.dto.UeditorBlogDTO;
import xyz.sanshan.main.service.vote.BlogVoteInfoService;

import java.util.*;

@Service
@Slf4j
public class BlogRecommendService {

    @Autowired
    private BlogVoteInfoService blogVoteInfoService;

    @Autowired
    private BlogIdGenerate blogIdGenerate;

    private final Integer generateBlogs = 5;


    /**
     * 生成推荐博客
     *
     * @return
     */
    public List<BaseBlogDTO> generateBlogs() {
        log.info("生成一次 博客推荐数据");

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

        return assembleResult(blogVoteDTOS, idMap);
    }

    /**
     * 组装结果
     *
     * @param blogVoteDTOS
     * @param idMap
     * @return
     */
    private List<BaseBlogDTO> assembleResult(Set<BlogVoteDTO> blogVoteDTOS, Map<Long, EditorTypeEnum> idMap) {
        List<BaseBlogDTO> recommendBlogs = new LinkedList<>();

        Map<Long, String> invertIdTitleMap = blogIdGenerate.getInvertIdTitleMap();
        //放到这里自动排序
        Iterator<BlogVoteDTO> it = blogVoteDTOS.iterator();
        int i = 0;

        //组装结果
        while (i < generateBlogs - 1 && it.hasNext()) {
            Long blogId = it.next().getBlogId();
            BaseBlogDTO blogDTO = null;

            EditorTypeEnum editorType = idMap.get(blogId);
            switch (editorType) {
                case MARKDOWN_EDITOR:
                    blogDTO = new MarkDownBlogDTO();
                    break;
                case UEDITOR_EDITOR:
                    blogDTO = new UeditorBlogDTO();
                    break;
                case VOID_ID:
                    break;
                default:
                    break;
            }
            blogDTO.setId(blogId);
            blogDTO.setTitle(invertIdTitleMap.get(blogId));
            recommendBlogs.add(i, blogDTO);
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
        b2Rate = excellentRate(b1Favours, b2Treads);

        //推荐比例
        double b1RecommendRate;
        b1RecommendRate = recommendRate(b1Votes, b1Rate);
        double b2RecommendRate;
        b2RecommendRate = recommendRate(b2Votes, b2Rate);

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
    private double excellentRate(Integer favours, Integer treads) {
        double rate = 1;
        //防止 /zero 异常
        if (favours == 0 && treads != 0) {
            rate = 1 / treads;
        } else if (favours != 0 && treads == 0) {
            rate = favours;
        } else if (favours == 0 & treads == 0) {
            rate = 1;
        } else {
            rate = favours / treads;
        }
        return rate;
    }


    /**
     * 推荐比例
     * 比例越大越好
     *
     * 非常简陋
     *
     * @param votes         投票总数
     * @param excellentRate 精品比例
     * @return
     */
    private double recommendRate(Integer votes, Double excellentRate) {
        double recommendRate;
        recommendRate = (excellentRate * excellentRate) * votes;
        return recommendRate;
    }


}
