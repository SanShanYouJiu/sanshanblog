package xyz.sanshan.main.web.controller.auth;

import com.google.code.kaptcha.Producer;
import xyz.sanshan.main.service.vo.CodeValidateVO;
import xyz.sanshan.main.service.vo.ResponseMsgVO;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@RestController()
public class CodeController {


     @Autowired
     private RedisTemplate<String,String>redisTemplate;

     private AtomicLong atomicLong = new AtomicLong(0);

    public static final String CODE_ID_PREFIX = "codeValidate:";

    private Producer captchaProducer = null;

    @Autowired
    public void setCaptchaProducer(Producer captchaProducer){
        this.captchaProducer = captchaProducer;
    }

    /**
     * 验证码 base64输出 很慢 并且会生成大量的对象
     * @param response
     * @throws IOException
     */
    @GetMapping("/codeValidate")
    public ResponseMsgVO getCode(HttpServletResponse response) throws IOException {
        ResponseMsgVO msgVO = new ResponseMsgVO();

        String capText = captchaProducer.createText();
        BufferedImage buffImg = captchaProducer.createImage(capText);

        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");

        //生成一个codeID对应 在三分钟内有效
        Long codeId=atomicLong.incrementAndGet();
        redisTemplate.opsForValue().set(CODE_ID_PREFIX+String.valueOf(codeId), capText,3, TimeUnit.MINUTES);

        //将图片转换为BASE64编码
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(buffImg, "png", os);

        byte[] bytes = os.toByteArray();
        String imageCode = "data:image/png;base64,"+Base64.encode(bytes);
        CodeValidateVO codeValidateVO = new CodeValidateVO(imageCode, codeId);
        return msgVO.buildOKWithData(codeValidateVO);
    }

}
