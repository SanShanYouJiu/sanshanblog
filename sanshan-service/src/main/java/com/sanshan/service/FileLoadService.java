package com.sanshan.service;

import com.mongodb.gridfs.GridFSDBFile;
import com.sanshan.dao.mongo.FileOperation;
import com.sanshan.dao.mongo.UserRepository;
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

@Service
@Slf4j
public class FileLoadService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileOperation fileOperation;


    public ResponseEntity<Resource> getFileImage(String filename) {
        File file = null;
        ResponseEntity<Resource> responseEntity = null;
        Resource body;
        GridFSDBFile gridFSDBFile = fileOperation.getFile(filename).get(0);
        try {
            file = new File("F://Temp//imageFile.jpg");
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
