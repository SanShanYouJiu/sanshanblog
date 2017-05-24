package com.sanshan.util.test;

import com.sanshan.util.AuthCodeUtil;
import org.junit.Test;

import java.awt.image.BufferedImage;

public class AuthCodeTest {
    private final static int width = 90;//验证码宽度
    private final static int height = 40;//验证码高度
    private final static int codeCount = 4;//验证码个数
    private final static int lineCount = 19;//混淆线个数

    @Test
    public void test1(){
        BufferedImage bfimg= new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        String resultCode = AuthCodeUtil.createCodeImage(bfimg, width, height, lineCount, codeCount);
//        FileUtils.write(new File("D://保存文件//ceshi.png"),);
        System.out.println(resultCode);
    }


}
