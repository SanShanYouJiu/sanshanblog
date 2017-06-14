package com.sanshan.web.controller.auth;

import com.sanshan.util.AuthCodeUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController()
public class CodeController {

    private final static int width = 90;//验证码宽度
    private final static int height = 40;//验证码高度
    private final static int codeCount = 4;//验证码个数
    private final static int lineCount = 19;//混淆线个数


    /**
     * 验证码
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/codeValidate")
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedImage buffImg= new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        String resultCode = AuthCodeUtil.createCodeImage(buffImg, width, height, lineCount, codeCount);
        HttpSession session = request.getSession();
        session.setAttribute("codeValidate",resultCode);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");
        //写回
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(buffImg,"png",sos);
    }



}
