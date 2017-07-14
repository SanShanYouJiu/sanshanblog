package com.sanshan.web.controller.editor;

import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.dto.MarkDownBlogDTO;
import com.sanshan.service.BlogService;
import com.sanshan.service.editor.MarkDownBlogService;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.exception.ERROR;
import com.sanshan.util.info.EditorTypeEnum;
import com.sanshan.util.info.PosCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("markdown-editor")
@RestController
@PreAuthorize("hasRole('USER')")
public class MarkDownEditorController {

    @Autowired
    private MarkDownBlogService markDownBlogService;

    @Autowired
    private BlogIdGenerate blogIdGenerate;

    @Autowired
    private BlogService blogService;

    /**
     * 分页查询
     *
     * @param pagenum
     * @param pagesize
     * @return
     */
    @RequestMapping(value = "query-by-page", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryByPage(@RequestParam("pagenum") Integer pagenum
            , @RequestParam("pagesize") Integer pagesize) {
        PageInfo<MarkDownBlogDTO> info = markDownBlogService.queryDtoPageListByWhere(null, pagenum, pagesize);
        ResponseMsgVO<PageInfo> responseMsgVO = new ResponseMsgVO<PageInfo>();
        return responseMsgVO.buildOKWithData(info);
    }


    @RequestMapping(value = "query-all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryAll() {
        ResponseMsgVO<List<BlogVO>> responseMsgVO = new ResponseMsgVO<>();
        List<BlogVO> list;
        list=blogService.queryAll().stream().filter(blogVO -> blogVO.getType()==1).collect(Collectors.toList());
        return responseMsgVO.buildOKWithData(list);
    }


    @RequestMapping(value = "insert-blog", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO insertMarkDownBlog(
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "title", required = false) String title) {

        int result = markDownBlogService.saveDO(content, title, tag);
        if (result ==0){
            return new ResponseMsgVO().buildError(new ERROR(500, "未存入成功"));
        }
        ResponseMsgVO responseMsgVO = new ResponseMsgVO().buildOK();
        return responseMsgVO;
    }


    @RequestMapping(value = "delete-by-id", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO deleteMarkDownBlog(@RequestParam("id") Long id) {
        if (blogIdGenerate.getType(id)==EditorTypeEnum.MarkDown_EDITOR){
            int result = markDownBlogService.deleteDOById(id);
            if (result == 0) {
                return new ResponseMsgVO().buildError(new ERROR(500, "未删除成功"));
            }
            //id去除匹配
            blogIdGenerate.remove(id);
        return new ResponseMsgVO().buildOK();
        }
        return new ResponseMsgVO().buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR,"该ID不是MarkdownEditor格式");
    }


    @RequestMapping(value = "update-by-id",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO update(
            @RequestParam(value = "id")Long id,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "title", required = false) String title) {
        if(blogIdGenerate.getType(id)!=EditorTypeEnum.MarkDown_EDITOR){
            return  new ResponseMsgVO().buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR,
                    "该ID对应的不是Markdown格式的文件");
        }
        markDownBlogService.updateSelectiveDO(id,content,title,tag);
        return new ResponseMsgVO().buildOK();
    }

}
