package com.sanshan.web.controller.editor;

import com.baidu.ueditor.ActionEnter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.dto.UEditorBlogDTO;
import com.sanshan.pojo.entity.UEditorBlogDO;
import com.sanshan.service.editor.UEditorFileService;
import com.sanshan.service.editor.UeditorBlogService;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.EditorTypeEnum;
import com.sanshan.util.info.PosCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("ueditor-editor")
public class UEditorEditorController {


    @Autowired
    private UEditorFileService fileService;

    @Autowired
    private UeditorBlogService uEditorBlogService;

    @Autowired
    private BlogIdGenerate blogIdGenerate;



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
    public void getUeditorFile(@PathVariable("format") String format,
                               @PathVariable("date") String date,
                               @PathVariable("filename") String filename,
                               @PathVariable("suffix") String suffix,
                               HttpServletRequest request,
                               HttpServletResponse response) {
       fileService.getUEditorFile(format,date,filename,suffix,response);

    }


    @RequestMapping(value = "query-by-page",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryByPage(@RequestParam("pagenum")Integer pagenum
            , @RequestParam("pagesize")Integer pagesize){
        ResponseMsgVO<PageInfo<UEditorBlogDTO>> responseMsgVO = new ResponseMsgVO<>();
        PageHelper.startPage(pagenum, pagesize);
        List<UEditorBlogDTO> list = uEditorBlogService.queryDtoAll();
        PageInfo<UEditorBlogDTO> info = new PageInfo(list);
        return responseMsgVO.buildOKWithData(info);
    }


    @RequestMapping(value = "query-all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryAll() {
        ResponseMsgVO<List<UEditorBlogDTO>> responseMsgVO = new ResponseMsgVO<>();
        List<UEditorBlogDTO> list = uEditorBlogService.queryDtoAll();
        return responseMsgVO.buildOKWithData(list);
    }




    @RequestMapping(value = "insert-blog", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO insertMarkDownBlog(
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "title", required = false) String title) {
        UEditorBlogDO uEditorBlogDO = new UEditorBlogDO();
        //使用IdMap生成的Id
        uEditorBlogDO.setId(blogIdGenerate.getId());
        uEditorBlogDO.setContent(content);
        uEditorBlogDO.setTitle(title);
        uEditorBlogDO.setCreated(new Date());
        uEditorBlogDO.setUpdated(new Date());
        uEditorBlogDO.setTag(tag);
        uEditorBlogDO.setTime(new Date());
        uEditorBlogDO.setUser("ceshi");
        //加入IdMap对应
        blogIdGenerate.addIdMap(blogIdGenerate.getId(), EditorTypeEnum.UEDITOR_EDITOR);
        uEditorBlogService.saveDO(uEditorBlogDO);
        ResponseMsgVO responseMsgVO = new ResponseMsgVO().buildOK();
        return responseMsgVO;
    }


    @RequestMapping(value = "delete-by-id", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO deleteUeditorBlog(@RequestParam("id") Long id) {
        if (blogIdGenerate.getType(id)==EditorTypeEnum.UEDITOR_EDITOR) {
            //id去除匹配
            blogIdGenerate.remove(id);
            uEditorBlogService.deleteDOById(id);
            return new ResponseMsgVO().buildOK();
        }
        return new ResponseMsgVO().buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR,"该ID不是UeditorEditor格式");
    }



    @RequestMapping(value = "update-by-id",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO update(
            @RequestParam(value = "id")Long id,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "title", required = false) String title) {
        UEditorBlogDO uEditorBlogDO = new UEditorBlogDO();
        uEditorBlogDO.setId(id);
        uEditorBlogDO.setContent(content);
        uEditorBlogDO.setUpdated(new Date());
        uEditorBlogDO.setTag(tag);
        uEditorBlogDO.setTitle(title);
        uEditorBlogService.updateSelectiveDO(uEditorBlogDO);
        return new ResponseMsgVO().buildOK();
    }




}
