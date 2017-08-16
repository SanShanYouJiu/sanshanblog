package com.sanshan.DaoTest;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.sanshan.dao.mongo.FileOperation;
import com.sanshan.web.config.javaconfig.MongoDBConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoDBConfig.class})
public class MongonTest {



    @Autowired
    FileOperation fileService;


    /**
     * 文件并不存在了 单纯测试
     */
    @Test
    public void imageSaveTest() throws FileNotFoundException {
        InputStream content = new FileInputStream(new File("C:\\Program Files\\nginx-1.13.3\\html\\assets\\images\\defaultUser.png"));
        DBObject metedata = new BasicDBObject();
        fileService.saveFile(content, "defaultUser.png","image/png", metedata);

    }

    @Test
    public void imageGetTest(){
        List<GridFSDBFile> gridFSDBFiles = fileService.getFile("/ueditor/upload/image/20170505/1493989483548094351.jpg");
        gridFSDBFiles.forEach((GridFSDBFile file)-> {
            System.out.println(file.getFilename());
            System.out.println(file.getInputStream());
            try {
                file.writeTo("D://新图片.jpg");
            } catch (IOException e) {
                System.out.println("代码出现问题"+e.getMessage());
                e.printStackTrace();
            }
        });
    }


    @Test
    public void imageDelete(){
        fileService.deleteFile("ueditor/upload/image/20170105/323124567312312321312.jpg");
    }

}
