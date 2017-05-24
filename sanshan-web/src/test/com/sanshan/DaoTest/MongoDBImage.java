package com.sanshan.DaoTest;

import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

public class MongoDBImage {
    
    public static void main(String[] args) {         
        try {

            Mongo mongo = new Mongo("localhost", 27017);
            DB db = mongo.getDB("sanshanblog");
            DBCollection collection = db.getCollection("MyImage"); //没有这个集合会默认创建一个
            String newFileName = "test-java-image";
            File imageFile = new File("D:\\ceshi.jpg");//需要创建一个图片
            // create a "photo" namespace
            GridFS gfsPhoto = new GridFS(db, "file");
            // get image file from local drive
            GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile); 
            // set a new filename for identify purpose
            gfsFile.setFilename(newFileName); 
            // save the image file into mongoDB
            gfsFile.save(); 
            // print the result
            DBCursor cursor = gfsPhoto.getFileList();
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }

            // get image file by it's filename
            GridFSDBFile imageForOutput = gfsPhoto.findOne(newFileName); 
            // save it into a new image file
            imageForOutput.writeTo("D:\\保存文件\\QQ-From-Mongodb.jpg");
            // remove the image file from mongoDB
            gfsPhoto.remove(gfsPhoto.findOne(newFileName));
            System.out.println("Done"); 
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}