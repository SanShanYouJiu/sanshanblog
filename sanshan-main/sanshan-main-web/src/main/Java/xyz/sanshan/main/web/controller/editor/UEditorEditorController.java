package xyz.sanshan.main.web.controller.editor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import xyz.sanshan.main.web.api.ueditor.ActionEnter;
import xyz.sanshan.common.info.EditorTypeEnum;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.service.editor.BlogIdGenerate;
import xyz.sanshan.main.service.editor.UEditorBlogService;
import xyz.sanshan.main.service.editor.UEditorFileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping("ueditor-editor")
public class UEditorEditorController {


    @Autowired
    private UEditorFileService fileService;

    @Autowired
    private UEditorBlogService uEditorBlogService;

    @Autowired
    private BlogIdGenerate blogIdGenerate;


    @Value("${ueditor-config.location}")
    private String ueditorConfigLocation;

    @RequestMapping("/config")
    public void config(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        // response.setContentType("application/json");
        request.setCharacterEncoding("utf-8");
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        response.setHeader("Content-Type", "text/html");
        try {
            String a = request.getRequestURI();
            String exec = new ActionEnter(request, rootPath, ueditorConfigLocation).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //@RequestMapping(value = "/upload/{format}/{date}/{filename}.{suffix}")
    //public void getUeditorFile(@PathVariable("format") String format,
    //                           @PathVariable("date") String date,
    //                           @PathVariable("filename") String filename,
    //                           @PathVariable("suffix") String suffix,
    //                           HttpServletResponse response) {
    //    fileService.getUEditorFile(format, date, filename, suffix, response);
    //
    //}



    @PostMapping(value = "blog", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO insertMarkDownBlog(
            @RequestParam(value = "content", required = false) String content,
        @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "tag", required = false) String tag) {

        int result = uEditorBlogService.saveDO(content, title, tag);
        if (result == 0) {
            return new ResponseMsgVO().buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR,"未存入成功");
        }
        ResponseMsgVO responseMsgVO = new ResponseMsgVO().buildOK();
        return responseMsgVO;
    }


    @DeleteMapping(value = "blog/id:{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO deleteUeditorBlog(@PathVariable("id") Long id) {
        if (blogIdGenerate.getType(id) == EditorTypeEnum.UEDITOR_EDITOR) {
            int result = uEditorBlogService.deleteDOById(id);
            if (result == 0) {
                return new ResponseMsgVO().buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, "未删除成功");
            }
            return new ResponseMsgVO().buildOK();
        }
        return new ResponseMsgVO().buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, "该ID对应的不是富文本格式的文件");
    }


    @PostMapping(value = "blog/id:{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO update(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "title") String title) {
        if (blogIdGenerate.getType(id) != EditorTypeEnum.UEDITOR_EDITOR) {
            return new ResponseMsgVO().buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR,
                    "该ID对应的不是富文本格式的文件");
        }
        uEditorBlogService.updateSelectiveDO(id,content,title,tag);
        return new ResponseMsgVO().buildOK();
    }


}
