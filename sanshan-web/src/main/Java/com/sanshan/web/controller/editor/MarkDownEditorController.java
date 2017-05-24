package com.sanshan.web.controller.editor;

import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.dto.MarkDownBlogDTO;
import com.sanshan.pojo.entity.MarkDownBlogDO;
import com.sanshan.service.editor.MarkDownBlogService;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.BlogOperationState;
import com.sanshan.util.info.EditorTypeEnum;
import com.sanshan.util.info.SanShanBlogInfoEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @RequestMapping("query-by-page")
    public Map<String,Object> QueryByPage(@RequestParam("pagenum")Integer pagenum
                                          ,@RequestParam("pagesize")Integer pagesize){
        Map<String,Object> maps =new HashMap();
        PageInfo<MarkDownBlogDTO> info = markDownBlogService.queryDtoPageListByWhere(null,pagenum,pagesize);
        maps.put("total", info.getTotal());
        maps.put("rows", info.getSize());
        return maps;
    }



    @RequestMapping("query-all")
    public List<MarkDownBlogDTO> QueryAll(){
        List<MarkDownBlogDTO> list= markDownBlogService.queryDtoAll();
        return list;
    }


    @RequestMapping("insert-blog")
    public BlogOperationState InsertMarkDownBlog(@RequestParam("markdown-blog") MarkDownBlogDO markDownBlog) {
        //id生成
        blogIdGenerate.setId(markDownBlog.getId(), EditorTypeEnum.MarkDown_EDITOR);
        markDownBlog.setId(blogIdGenerate.getId());
        markDownBlogService.saveDO(markDownBlog);
        BlogOperationState sanShanBlogState;
        sanShanBlogState = new BlogOperationState(200, SanShanBlogInfoEnum.SAVE_SUCCESS.getValue());
        return sanShanBlogState;
    }



     @RequestMapping("delete-blog-by-id")
     public BlogOperationState DeleteMarkDownBlog(@RequestParam("id")Long id){
        //id去除
         blogIdGenerate.remove(id);
         markDownBlogService.deleteDOById(id);
         BlogOperationState sanShanBlogState;
         sanShanBlogState=new BlogOperationState(200, SanShanBlogInfoEnum.DELETE_SUCCESS.getValue());
         return  sanShanBlogState;
    }



}
