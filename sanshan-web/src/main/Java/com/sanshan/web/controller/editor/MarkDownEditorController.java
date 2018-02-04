package com.sanshan.web.controller.editor;

import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.dto.MarkDownBlogDTO;
import com.sanshan.service.BlogService;
import com.sanshan.service.editor.MarkDownBlogService;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.EditorTypeEnum;
import com.sanshan.util.info.PosCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("markdown-editor")
@RestController
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
     * @param pageNum
     * @param pageRows
     * @return
     */
    @GetMapping(value = "blog/page/pageRows:{pageRows}/pageNum:{pageNum}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryByPage(@PathVariable("pageNum") Integer pageNum
            , @PathVariable("pageRows") Integer pageRows) {
        PageInfo<MarkDownBlogDTO> info = markDownBlogService.queryDtoPageListByWhere(null, pageRows, pageNum);
        ResponseMsgVO<PageInfo> responseMsgVO = new ResponseMsgVO<PageInfo>();
        return responseMsgVO.buildOKWithData(info);
    }


    @GetMapping(value = "blog/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryAll() {
        ResponseMsgVO<List<BlogVO>> responseMsgVO = new ResponseMsgVO<>();
        List<BlogVO> list;
        list=blogService.queryAll().stream().filter(blogVO -> blogVO.getType()==1).collect(Collectors.toList());
        return responseMsgVO.buildOKWithData(list);
    }


    @PostMapping(value = "blog", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO insertMarkDownBlog(
            @RequestParam(value = "content") String content,
            @RequestParam(value = "tag",required = false) String tag,
            @RequestParam(value = "title") String title) {

        int result = markDownBlogService.saveDO(content, title, tag);
        if (result ==0){
            return new ResponseMsgVO().buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR,"未存入成功");
        }
        ResponseMsgVO responseMsgVO = new ResponseMsgVO().buildOK();
        return responseMsgVO;
    }


    @DeleteMapping(value = "blog/id:{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO deleteMarkDownBlog(@PathVariable("id") Long id) {
        if (blogIdGenerate.getType(id)==EditorTypeEnum.MARKDOWN_EDITOR){
            int result = markDownBlogService.deleteDOById(id);
            if (result == 0) {
                return new ResponseMsgVO().buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR,"未删除成功");
            }
        return new ResponseMsgVO().buildOK();
        }
        return new ResponseMsgVO().buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR,"该ID不是MarkdownEditor格式");
    }


    @PostMapping(value = "blog/id:{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO update(
            @PathVariable(value = "id")Long id,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "title", required = false) String title) {
        if(blogIdGenerate.getType(id)!=EditorTypeEnum.MARKDOWN_EDITOR){
            return  new ResponseMsgVO().buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR,
                    "该ID对应的不是Markdown格式的文件");
        }
        markDownBlogService.updateSelectiveDO(id,content,title,tag);
        return new ResponseMsgVO().buildOK();
    }

}
