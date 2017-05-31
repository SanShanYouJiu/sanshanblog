package com.sanshan.web.controller.editor;

import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.dto.MarkDownBlogDTO;
import com.sanshan.pojo.entity.MarkDownBlogDO;
import com.sanshan.service.editor.MarkDownBlogService;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.EditorTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RequestMapping("markdown-editor")
@RestController
public class MarkDownEditorController {

    @Autowired
    private BlogIdGenerate blogIdGenerate;

    @Autowired
    private MarkDownBlogService markDownBlogService;

    /**
     * 分页查询
     * @param pagenum
     * @param pagesize
     * @return
     */
    @RequestMapping(value = "query-by-page")
    public ResponseMsgVO QueryByPage(@RequestParam("pagenum")Integer pagenum
                                          ,@RequestParam("pagesize")Integer pagesize){
        PageInfo<MarkDownBlogDTO> info = markDownBlogService.queryDtoPageListByWhere(null,pagenum,pagesize);
        ResponseMsgVO<PageInfo> responseMsgVO= new ResponseMsgVO<PageInfo>();
        return  responseMsgVO.buildOKWithData(info);
    }



    @RequestMapping(value = "query-all")
    public ResponseMsgVO QueryAll(){
        ResponseMsgVO<List<MarkDownBlogDTO>> responseMsgVO = new ResponseMsgVO<>();
        List<MarkDownBlogDTO> list = markDownBlogService.queryDtoAll();
        return responseMsgVO.buildOKWithData(list);
    }


    @RequestMapping(value = "insert-blog",method = RequestMethod.POST)
    public ResponseMsgVO InsertMarkDownBlog(@RequestParam("id") Long id,@RequestParam("content") String content,@RequestParam("tag") String tag
            ,@RequestParam("user") String user) {
        //id生成
        MarkDownBlogDO markDownBlog = new MarkDownBlogDO();
        markDownBlog.setId(id);
        markDownBlog.setContent(content);
        markDownBlog.setCreated(new Date());
        markDownBlog.setUpdated(new Date());
        markDownBlog.setTag(tag);
        markDownBlog.setTime(new Date());
        markDownBlog.setUser(user);
        blogIdGenerate.setId(markDownBlog.getId(), EditorTypeEnum.MarkDown_EDITOR);
        markDownBlog.setId(blogIdGenerate.getId());
        markDownBlogService.saveDO(markDownBlog);
         ResponseMsgVO responseMsgVO =new ResponseMsgVO().buildOK();
        return responseMsgVO;
    }



     @RequestMapping(value = "delete-by-id",method = RequestMethod.POST)
     public ResponseMsgVO DeleteMarkDownBlog(@RequestParam("id")Long id){
        //id去除
         blogIdGenerate.remove(id);
         markDownBlogService.deleteDOById(id);
         return  new ResponseMsgVO().buildOK();
    }


}
