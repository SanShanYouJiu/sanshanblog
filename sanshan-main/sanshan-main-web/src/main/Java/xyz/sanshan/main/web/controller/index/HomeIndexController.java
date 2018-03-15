package xyz.sanshan.main.web.controller.index;

import xyz.sanshan.main.service.FeedBackService;
import xyz.sanshan.main.service.vo.ResponseMsgVO;
import xyz.sanshan.main.web.config.javaconfig.auxiliary.MultipartFileBucketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller()
@RequestMapping("/index")
public class HomeIndexController {


    @Autowired
    private MultipartFileBucketValidator multipartFileBucketValidator;


    @Autowired
    private FeedBackService feedBackService;

    //作校验使用
    @InitBinder("multipartFileBucket")
    protected void initBinderMultipartFileBucket(WebDataBinder binder) {
        binder.setValidator(multipartFileBucketValidator);
    }


    @PostMapping(value = "/advice", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseMsgVO handleAdvice(@RequestParam(value = "email") String email, @RequestParam(value = "opinion", required = false) String opinion, @RequestHeader(value = "X-Real-IP") String ip) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        responseMsgVO.buildOK();
        feedBackService.saveBaseInfo(email, opinion);
        return responseMsgVO;
    }


    /**
     * 关闭这个方法 因为带宽不够用 不给匿名用户上传文件
     * @param ip
     * @param email
     * @param opinion
     * @param multipartFile
     * @return
     */
    //@RequestMapping(value = "/advice/file", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = {MediaType.IMAGE_PNG_VALUE,MediaType.IMAGE_GIF_VALUE,MediaType.IMAGE_JPEG_VALUE})
    //@ResponseBody
    public ResponseMsgVO handleFileUpload2(
            @RequestHeader(value = "X-Real-IP") String ip,
            @RequestHeader(value = "email") String email,
            @RequestHeader(value = "opinion") String opinion,
            @RequestParam(name = "file") MultipartFile multipartFile) {
        ResponseMsgVO responseMsgVO = new ResponseMsgVO();
        feedBackService.saveBaseInfo(email, "反馈文件:" + multipartFile.getOriginalFilename());
        feedBackService.saveFile(email, opinion, multipartFile);
        return responseMsgVO.buildOK();
    }

}
