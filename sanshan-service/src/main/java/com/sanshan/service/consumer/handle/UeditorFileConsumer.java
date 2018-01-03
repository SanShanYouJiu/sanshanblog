package com.sanshan.service.consumer.handle;

import com.sanshan.dao.UeditorFileQuoteMapper;
import com.sanshan.dao.UeditorIdFileMapMapper;
import com.sanshan.pojo.dto.UeditorFileQuoteDTO;
import com.sanshan.pojo.dto.UeditorIdFileMapDTO;
import com.sanshan.pojo.entity.UeditorFileQuoteDO;
import com.sanshan.pojo.entity.UeditorIdFileMapDO;
import com.sanshan.service.editor.UeditorFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author sanshan
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
public class UeditorFileConsumer {

    @Autowired
    private UeditorIdFileMapMapper ueditorIdFileMapMapper;

    @Autowired
    private UeditorFileQuoteMapper ueditorFileQuoteMapper;

    private ExecutorService pool = new ThreadPoolExecutor(1,1,10, TimeUnit.MINUTES,new LinkedBlockingDeque<>(),(r)->{
        Thread t = new Thread(r);
        t.setName("ueditor-consumer-thread");
        return t;
    });


    protected void ueditorConsumer(){
        if (!UeditorFileService.ueditorFileAddQueue.isEmpty() || !UeditorFileService.ueditorFileDecrQueue.isEmpty() || !UeditorFileService.ueditorFileUpload.isEmpty()) {
            pool.execute(() -> {
                ueditorBlogFileAdd();
                ueditorBlogFileDecr();
                ueditorFileUpload();
            });
        }
    }

    /**
     *博客文件的新增 对应的文件引用数增加以及该ID文件映射表的添加
     */
    private void ueditorBlogFileAdd(){
         while (!UeditorFileService.ueditorFileAddQueue.isEmpty()){
             UeditorIdFileMapDTO ueditorIdFileMapDTO = UeditorFileService.ueditorFileAddQueue.poll();
             List<String> filenames = ueditorIdFileMapDTO.getFilenames();
             for (int i = 0; i < filenames.size() ; i++) {
                 String filename = filenames.get(i);
                 ueditorIdFileMapMapper.insert(new UeditorIdFileMapDO(ueditorIdFileMapDTO.getBlog_id(), filename,new Date(),new Date()));
                 ueditorFileQuoteMapper.incrFilenameQuote(filename,new Date());
             }
         }
    }


    /**
     * 博客文件的删除 对应的文件引用数减少以及该ID文件映射表的删除
     */
    private void ueditorBlogFileDecr(){
        while (!UeditorFileService.ueditorFileDecrQueue.isEmpty()) {
            UeditorIdFileMapDTO ueditorIdFileMapDTO = UeditorFileService.ueditorFileDecrQueue.poll();
            List<String> filenames = ueditorIdFileMapDTO.getFilenames();
            ueditorIdFileMapMapper.deleteById(ueditorIdFileMapDTO.getBlog_id());
            for (int i = 0; i < filenames.size() ; i++) {
                String filename = filenames.get(i);
                ueditorFileQuoteMapper.decrFilenameQuote(filename,new Date());
            }
        }
    }

    /**
     * 所有上传的文件在这里进行记录 在定时检查中进行清除
     */
    private void ueditorFileUpload(){
        while (!UeditorFileService.ueditorFileUpload.isEmpty()) {
            UeditorFileQuoteDTO fileQuoteDTO = UeditorFileService.ueditorFileUpload.poll();
            ueditorFileQuoteMapper.insert(new UeditorFileQuoteDO(fileQuoteDTO.getFilename(), fileQuoteDTO.getQuote(), new Date(), new Date()));
        }

    }

}
