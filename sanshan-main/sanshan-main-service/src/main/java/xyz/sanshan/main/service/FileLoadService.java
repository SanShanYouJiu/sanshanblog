package xyz.sanshan.main.service;

import com.mongodb.gridfs.GridFSDBFile;
import xyz.sanshan.main.dao.mongo.FileOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * 功能关闭
 */
@Service
@Slf4j
public class FileLoadService {


    @Autowired
    private FileOperation fileOperation;


    public ResponseEntity<Resource> getFileImage(String filename) {
        File file = null;
        ResponseEntity<Resource> responseEntity = null;
        Resource body;
        GridFSDBFile gridFSDBFile = fileOperation.getFile(filename).get(0);
        try {
            //FIXME 这里的值会随着不同的系统而改变
            file = new File("/etc/sanshan-main/imageFile/imageFile.jpg");
            gridFSDBFile.writeTo(file);
            body = new FileSystemResource(file);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            responseHeaders.setContentDispositionFormData("attachment", filename);
            responseHeaders.setContentLength(file.length());
            responseHeaders.set("server-md5", gridFSDBFile.getMD5());

            responseEntity = new ResponseEntity<Resource>(body, responseHeaders, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseEntity;
    }


}
