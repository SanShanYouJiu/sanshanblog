package com.sanshan.web.controller.editor;

import com.baidu.ueditor.ActionEnter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.dto.UeditorBlogDTO;
import com.sanshan.service.BlogService;
import com.sanshan.service.editor.UeditorFileService;
import com.sanshan.service.editor.UeditorBlogService;
import com.sanshan.service.vo.BlogVO;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("ueditor-editor")
public class UeditorEditorController {


    @Autowired
    private UeditorFileService fileService;

    @Autowired
    private UeditorBlogService uEditorBlogService;

    @Autowired
    private BlogIdGenerate blogIdGenerate;

    @Autowired
    private BlogService blogService;

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
                               HttpServletResponse response) {
        fileService.getUEditorFile(format, date, filename, suffix, response);

    }


    @RequestMapping(value = "query-by-page", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryByPage(@RequestParam("pagenum") Integer pagenum
            , @RequestParam("pagesize") Integer pagesize) {
        ResponseMsgVO<PageInfo<UeditorBlogDTO>> responseMsgVO = new ResponseMsgVO<>();
        PageHelper.startPage(pagenum, pagesize);
        List<UeditorBlogDTO> list = uEditorBlogService.queryDtoAll();
        PageInfo<UeditorBlogDTO> info = new PageInfo(list);
        return responseMsgVO.buildOKWithData(info);
    }


    @RequestMapping(value = "query-all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO queryAll() {
        ResponseMsgVO<List<BlogVO>> responseMsgVO = new ResponseMsgVO<>();
        List<BlogVO> list ;
        list=blogService.queryAll().stream().filter(blogVO -> blogVO.getType()==0).collect(Collectors.toList());
        return responseMsgVO.buildOKWithData(list);
    }


    @RequestMapping(value = "insert-blog", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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


    @RequestMapping(value = "delete-by-id", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO deleteUeditorBlog(@RequestParam("id") Long id) {
        if (blogIdGenerate.getType(id) == EditorTypeEnum.UEDITOR_EDITOR) {
            int result = uEditorBlogService.deleteDOById(id);
            if (result == 0) {
                return new ResponseMsgVO().buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, "未删除成功");
            }
            return new ResponseMsgVO().buildOK();
        }
        return new ResponseMsgVO().buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, "该ID对应的不是富文本格式的文件");
    }


    @RequestMapping(value = "update-by-id", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMsgVO update(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "title", required = false) String title) {
        if (blogIdGenerate.getType(id) != EditorTypeEnum.UEDITOR_EDITOR) {
            return new ResponseMsgVO().buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR,
                    "该ID对应的不是富文本格式的文件");
        }
        uEditorBlogService.updateSelectiveDO(id,content,title,tag);
        return new ResponseMsgVO().buildOK();
    }


}
