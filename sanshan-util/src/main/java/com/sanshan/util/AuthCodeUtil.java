package com.sanshan.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 */
public class AuthCodeUtil {

    /**
     *
     验证码来源,去掉了混淆的O
     */
    private static final char[] CODE_SEQUENCE = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    private static final Random r = new Random();

    /**
     * 创建验证码
     * @param buffImg 放入的输出流
     * @param width 宽度
     * @param height 高度
     * @param lineCount 混淆线条数
     * @param codeCount 验证码数
     * @return 验证码
     */
    public static String createCodeImage(BufferedImage buffImg, int width, int height, int lineCount, int codeCount){
        //得到画笔
        Graphics g = buffImg.getGraphics();
        //1.设置颜色,画边框
        g.setColor(new Color(0.4235f,0.5765f,0.5647f,1f));
        g.drawRect(0,0,width,height);
        //2.设置颜色,填充内部
        g.setColor(new Color(0.4235f,0.5765f,0.5647f,1f));
        g.fillRect(1,1,width-2,height-2);
        //3.设置干扰线
        g.setColor(Color.gray);
        for (int i = 0; i < lineCount; i++) {
            g.drawLine(r.nextInt(width),r.nextInt(width),r.nextInt(width),r.nextInt(width));
        }
        //4.设置验证码
        g.setColor(Color.blue);
        //4.1设置验证码字体
        g.setFont(new Font("宋体",Font.BOLD|Font.ITALIC,30));
        return getCode(codeCount, g);
    }

    /**
     * 得到验证码
     * @param codeCount 验证码数量
     * @param g
     */
    private static String getCode(int codeCount, Graphics g) {
        StringBuilder builderCode = new StringBuilder();
        for (int i = 0; i < codeCount; i++) {
            char c = CODE_SEQUENCE[r.nextInt(CODE_SEQUENCE.length)];
            builderCode.append(c);
            g.drawString(c+"",15*(i+1),30);
        }
        return builderCode.toString();
    }


    /**
     * 得到验证码
     * @param codeCount 数量
     */
    public static String getCode(int codeCount) {
        StringBuilder builderCode = new StringBuilder();
        for (int i = 0; i < codeCount; i++) {
            builderCode.append(CODE_SEQUENCE[r.nextInt(CODE_SEQUENCE.length)]);
        }
        return builderCode.toString();
    }


}