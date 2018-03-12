package xyz.sanshan.service.consumer.accept;

import xyz.sanshan.dao.mybatis.UeditorFileQuoteMapper;
import xyz.sanshan.dao.mybatis.UeditorIdFileMapMapper;
import xyz.sanshan.pojo.dto.UeditorFileQuoteDTO;
import xyz.sanshan.pojo.dto.UeditorIdFileMapDTO;
import xyz.sanshan.pojo.entity.UeditorFileQuoteDO;
import xyz.sanshan.pojo.entity.UeditorIdFileMapDO;
import xyz.sanshan.service.editor.UeditorFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author sanshan
 */
@Service
@Slf4j
public class UeditorFileConsumer {

    @Autowired
    private UeditorIdFileMapMapper ueditorIdFileMapMapper;

    @Autowired
    private UeditorFileQuoteMapper ueditorFileQuoteMapper;


    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void ueditorConsumer(){
        if (!UeditorFileService.ueditorFileAddQueue.isEmpty() || !UeditorFileService.ueditorFileDecrQueue.isEmpty() || !UeditorFileService.ueditorFileUpload.isEmpty()) {
            if (log.isDebugEnabled()){
                log.debug("对ueditor中博客文件数据以及相关元数据的改变存入到数据库中");
            }
                ueditorFileExecute();
        }
    }


    private void ueditorFileExecute(){
        ueditorBlogFileAdd();
        ueditorBlogFileDecr();
        ueditorFileUpload();
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
