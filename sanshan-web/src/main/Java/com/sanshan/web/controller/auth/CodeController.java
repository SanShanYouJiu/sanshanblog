package com.sanshan.web.controller.auth;

import com.sanshan.service.vo.CodeValidateVO;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.AuthCodeUtil;
import com.sanshan.util.ImageBase64Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@RestController()
public class CodeController {

    private final static int width = 90;//验证码宽度
    private final static int height = 40;//验证码高度
    private final static int codeCount = 4;//验证码个数
    private final static int lineCount = 19;//混淆线个数

     @Autowired
     private RedisTemplate<String,String>redisTemplate;

     private AtomicLong atomicLong = new AtomicLong(0);

    public static final String codeIdCachePrefix = "codeValidate:";

    /**
     * 验证码
     * @param response
     * @throws IOException
     */
    @RequestMapping("/codeValidate")
    public ResponseMsgVO getCode(HttpServletResponse response) throws IOException {
        ResponseMsgVO msgVO = new ResponseMsgVO();

        BufferedImage buffImg= new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        String resultCode = AuthCodeUtil.createCodeImage(buffImg, width, height, lineCount, codeCount);
        //将resultCode存入session
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");

        //生成一个codeID对应 在三分钟内有效
        Long codeId=atomicLong.incrementAndGet();
        redisTemplate.opsForValue().set(codeIdCachePrefix+String.valueOf(codeId), resultCode,3, TimeUnit.MINUTES);

        //将图片转换为BASE64编码
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(buffImg, "png", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        String data= ImageBase64Utils.imageToBase64(is);
        CodeValidateVO codeValidateVO = new CodeValidateVO(data, codeId);
        return msgVO.buildOKWithData(codeValidateVO);
    }

}
