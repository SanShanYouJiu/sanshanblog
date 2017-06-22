package com.sanshan.DaoTest.SPEL;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * SPEL测试
 */
public class SpelTest {

    @Test
    public void test(){
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'!=null");
        boolean message = (Boolean) exp.getValue();
        System.out.println(message);
    }
}
