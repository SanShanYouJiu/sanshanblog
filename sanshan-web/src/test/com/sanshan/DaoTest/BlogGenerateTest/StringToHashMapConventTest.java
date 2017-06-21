package com.sanshan.DaoTest.BlogGenerateTest;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class StringToHashMapConventTest {

    @Test
    public void test(){
        HashMap map = new HashMap();
        String idmaps = "{1=MarkDown_EDITOR, 2=MarkDown_EDITOR, 3=UEDITOR_EDITOR, 4=void_Id}";
        try {
            String s = FileUtils.readFileToString(new File("D://Tim//Idmap.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
