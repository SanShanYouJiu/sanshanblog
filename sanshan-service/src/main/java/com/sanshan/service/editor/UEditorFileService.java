package com.sanshan.service.editor;

import com.mongodb.gridfs.GridFSDBFile;
import com.sanshan.dao.mongo.FileOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Slf4j
@Service
public class UEditorFileService {

    @Autowired
    FileOperation fileOperation;


    /**
     * mongoDB存入图片
     * @param content  文件
     * @param filename 文件名
     * @param type 类型
     * @param metedata 元数据 元数据其实就是数据的数据 其他附带的一些信息
     */
    public void saveFile(InputStream content, String filename, String type, Object metedata) {
        fileOperation.saveFile(content, filename, type, metedata);
    }

    /**
     *  获得Ueditor上传的文件
     *
     * @param format   上传的具体文件 是file还是image或者video
     * @param date     时间
     * @param filename 文件名
     * @param suffix   具体文件的后缀格式
     * @param response 当前环境的HttpServletResponse
     */
    public void getUEditorFile(String format, String date, String filename, String suffix, HttpServletResponse response){
        String FileFullName = "/api/ueditor-editor/upload/" + format + "/" + date + "/" + filename + "." + suffix;//这里注意 前缀是写死的
        response.setContentType(format+"\\"+suffix);
        List<GridFSDBFile> gridFSDBFiles = fileOperation.getFile(FileFullName);
        GridFSDBFile Gfile = gridFSDBFiles.get(0);
        try {
            OutputStream ot = response.getOutputStream();
            byte[] btImg = readStream(Gfile.getInputStream());
            ot.write(btImg);
            ot.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     *  mongoDB中获得图片
     * @param filename 文件名
     * @return 返回图片集合
     */
    public List<GridFSDBFile> getFile(String filename) {
        return fileOperation.getFile(filename);
    }




    /**
     * 删除图片
     * @param filename 文件名
     */
    public void deleteFile(String filename){
        fileOperation.deleteFile(filename);
    }


    public byte[] readStream(InputStream inStream) {
        ByteArrayOutputStream bops = new ByteArrayOutputStream();
        int data = -1;
        try {
            while((data = inStream.read()) != -1){
                bops.write(data);
            }
            return bops.toByteArray();
        }catch(Exception e){
            return null;
        }
    }


}
