package xyz.sanshan.main.service.consumer.accept;

import xyz.sanshan.main.dao.mybatis.UEditorFileQuoteMapper;
import xyz.sanshan.main.dao.mybatis.UEditorIdFileMapMapper;
import xyz.sanshan.main.pojo.dto.UEditorFileQuoteDTO;
import xyz.sanshan.main.pojo.dto.UEditorIdFileMapDTO;
import xyz.sanshan.main.pojo.entity.UEditorFileQuoteDO;
import xyz.sanshan.main.pojo.entity.UEditorIdFileMapDO;
import xyz.sanshan.main.service.editor.UEditorFileService;
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
public class UEditorFileConsumer {

    @Autowired
    private UEditorIdFileMapMapper UEditorIdFileMapMapper;

    @Autowired
    private UEditorFileQuoteMapper UEditorFileQuoteMapper;


    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void ueditorConsumer(){
        if (!UEditorFileService.ueditorFileAddQueue.isEmpty() || !UEditorFileService.ueditorFileDecrQueue.isEmpty() || !UEditorFileService.ueditorFileUpload.isEmpty()) {
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
         while (!UEditorFileService.ueditorFileAddQueue.isEmpty()){
             UEditorIdFileMapDTO UEditorIdFileMapDTO = UEditorFileService.ueditorFileAddQueue.poll();
             List<String> filenames = UEditorIdFileMapDTO.getFilenames();
             for (int i = 0; i < filenames.size() ; i++) {
                 String filename = filenames.get(i);
                 UEditorIdFileMapMapper.insert(new UEditorIdFileMapDO(UEditorIdFileMapDTO.getBlog_id(), filename,new Date(),new Date()));
                 UEditorFileQuoteMapper.incrFilenameQuote(filename,new Date());
             }
         }
    }


    /**
     * 博客文件的删除 对应的文件引用数减少以及该ID文件映射表的删除
     */
    private void ueditorBlogFileDecr(){
        while (!UEditorFileService.ueditorFileDecrQueue.isEmpty()) {
            UEditorIdFileMapDTO UEditorIdFileMapDTO = UEditorFileService.ueditorFileDecrQueue.poll();
            List<String> filenames = UEditorIdFileMapDTO.getFilenames();
            UEditorIdFileMapMapper.deleteById(UEditorIdFileMapDTO.getBlog_id());
            for (int i = 0; i < filenames.size() ; i++) {
                String filename = filenames.get(i);
                UEditorFileQuoteMapper.decrFilenameQuote(filename,new Date());
            }
        }
    }

    /**
     * 所有上传的文件在这里进行记录 在定时检查中进行清除
     */
    private void ueditorFileUpload(){
        while (!UEditorFileService.ueditorFileUpload.isEmpty()) {
            UEditorFileQuoteDTO fileQuoteDTO = UEditorFileService.ueditorFileUpload.poll();
            UEditorFileQuoteMapper.insert(new UEditorFileQuoteDO(fileQuoteDTO.getFilename(), fileQuoteDTO.getQuote(), new Date(), new Date()));
        }

    }

}
