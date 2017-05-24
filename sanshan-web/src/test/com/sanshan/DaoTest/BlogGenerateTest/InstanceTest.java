package com.sanshan.DaoTest.BlogGenerateTest;

import com.sanshan.pojo.entity.EditorDO;
import com.sanshan.pojo.entity.MarkDownBlogDO;
import com.sanshan.pojo.entity.UEditorBlogDO;
import org.junit.Test;

public class InstanceTest {

    @Test
    public void test() {
        EditorDO blog = new UEditorBlogDO();
        if (blog instanceof MarkDownBlogDO) {
            System.out.println("是的");
        }
    }
}
