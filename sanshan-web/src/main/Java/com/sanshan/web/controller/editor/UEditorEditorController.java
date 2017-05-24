package com.sanshan.web.controller.editor;

import com.baidu.ueditor.ActionEnter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.dto.UEditorBlogDTO;
import com.sanshan.pojo.entity.UEditorBlogDO;
import com.sanshan.service.editor.UEditorFileService;
import com.sanshan.service.editor.UeditorBlogService;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.BlogOperationState;
import com.sanshan.util.info.EditorTypeEnum;
import com.sanshan.util.info.SanShanBlogInfoEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("ueditor-editor")
public class UEditorEditorController {

    public static String temp;

    @Autowired
    private UEditorFileService fileService;

    @Autowired
    private UeditorBlogService uEditorBlogService;

    @Autowired
    private BlogIdGenerate blogIdGenerate;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView UEditor() {
        return new ModelAndView("Editor/ueditor");
    }


    @RequestMapping("/config")
    public void config(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        // response.setContentType("application/json");
        request.setCharacterEncoding("utf-8");
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        response.setHeader("Content-Type", "text/html");
        try {
            String a = request.getRequestURI();
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @RequestMapping(value = "/upload/{format}/{date}/{filename}.{suffix}")
    public void GetUeditorFile(@PathVariable("format") String format,
                               @PathVariable("date") String date,
                               @PathVariable("filename") String filename,
                               @PathVariable("suffix") String suffix,
                               HttpServletRequest request,
                               HttpServletResponse response) {
       fileService.getUEditorFile(format,date,filename,suffix,response);

    }


    @RequestMapping("query-by-page")
    public Map<String,Object> QueryByPage(@RequestParam("pagenum")Integer pagenum
            , @RequestParam("pagesize")Integer pagesize){
        Map<String,Object> maps =new HashMap();
        PageHelper.startPage(pagenum, pagesize);
        List<UEditorBlogDTO> list = uEditorBlogService.queryDtoAll();
        PageInfo<UEditorBlogDTO> info = new PageInfo(list);
        maps.put("total", info.getTotal());
        maps.put("rows", info.getSize());
        return maps;
    }



    @RequestMapping("query-all")
    public List<UEditorBlogDTO> QueryAll(){
        List<UEditorBlogDTO> list= uEditorBlogService.queryDtoAll();
        return list;
    }



    @RequestMapping(value = "/insert-blog")
    public BlogOperationState InsertUeditorBlog(@RequestParam("ueditor-blog")UEditorBlogDO uEditorBlog) {
        //Id生成器
        blogIdGenerate.setId(uEditorBlog.getId(), EditorTypeEnum.UEDITOR_EDITOR);
        uEditorBlog.setId(blogIdGenerate.getId());
        uEditorBlogService.saveDO(uEditorBlog);
        BlogOperationState sanShanBlogState;
        sanShanBlogState=new BlogOperationState(200, SanShanBlogInfoEnum.SAVE_SUCCESS.getValue());
        return  sanShanBlogState;
    }



    @RequestMapping(value = "/delete-blog-by-id")
    public BlogOperationState InsertUeditorBlog(@RequestParam("id")Long id) {
        blogIdGenerate.remove(id);
        uEditorBlogService.deleteDOById(id);
        BlogOperationState sanShanBlogState;
        sanShanBlogState=new BlogOperationState(200, SanShanBlogInfoEnum.DELETE_SUCCESS.getValue());
        return  sanShanBlogState;
    }





}
