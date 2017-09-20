package com.sanshan.web.controller.admin;

import com.sanshan.service.FileLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/file")
public class FileLoadController {

    @Autowired
    private FileLoadService fileLoadService;

    @RequestMapping(value = "/images/{filename}.{suffix}")
    //用作用户自定义头像加载
    //暂时给任何用户观看
    //注意在使用ResponseEntity时不能加 produces选项 因为ResponseEntity与produces冲突
    public ResponseEntity<Resource> getFileImage(@PathVariable(value = "filename") String filename, @PathVariable(value = "suffix") String suffix) throws IOException {
        return fileLoadService.getFileImage(filename + "." + suffix);
    }


}
