package xyz.sanshan.main.service.recommend;

import xyz.sanshan.main.pojo.dto.BaseBlogEditorDTO;
import xyz.sanshan.main.pojo.dto.BlogVoteDTO;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.service.vote.BlogVoteInfoService;
import xyz.sanshan.common.BlogIdGenerate;
import xyz.sanshan.common.info.EditorTypeEnum;
import xyz.sanshan.common.info.PosCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BlogRecommendService {

    @Autowired
    private BlogVoteInfoService blogVoteInfoService;

    @Autowired
    private BlogIdGenerate blogIdGenerate;

    private  final Integer generateBlogs = 5;


    /**
     * TODO 完成博客推荐
     * @return
     */
    public List<BaseBlogEditorDTO> generateBlogs(){
        List<BaseBlogEditorDTO> recommendBlogs = new LinkedList<>();
        log.info("生成一次 博客推荐数据");

        Map<Long, EditorTypeEnum> idMap = blogIdGenerate.getIdCopy();
        //放到这里自动排序
        //Set<BlogVoteDTO> blogVoteDTOS = new TreeSet<BlogVoteDTO>((e1, e2)->{

        //});
        for (Map.Entry<Long, EditorTypeEnum> entry : idMap.entrySet()) {
            Long blogId = entry.getKey();
            ResponseMsgVO responseMsgVO = new ResponseMsgVO();
            blogVoteInfoService.queryBlogInfo(blogId, responseMsgVO);
            if (responseMsgVO.getStatus().equals(PosCodeEnum.OK.getStatus())){
                BlogVoteDTO blogVoteDTO = (BlogVoteDTO) responseMsgVO.getData();
                //blogVoteDTOS.add(blogVoteDTO);
            }else {
                continue;
            }
        }

        return recommendBlogs;
    }

}
