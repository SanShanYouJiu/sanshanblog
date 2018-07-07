package xyz.sanshan.main.service.task.night;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import xyz.sanshan.main.dao.mybatis.UEditorFileQuoteMapper;
import xyz.sanshan.main.service.editor.UEditorFileService;

import java.util.Map;

/**
 */
@Component
@Slf4j
public class UEditorFileUploadCheck {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UEditorFileService UEditorFileService;

    @Autowired
    private UEditorFileQuoteMapper UEditorFileQuoteMapper;

    //TODO: 这里可以考虑改造为spring batch来处理
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void check(){
       Map<String,Integer> fileQuoteMap= redisTemplate.opsForHash().entries(UEditorFileService.UEDITOR_UPLOAD_FILE);
       //首先检查引用为0的 然后查看引用0的在UEDITOR_UPLOAD_TEMP_FILE缓存中是否存在
        for(Map.Entry<String,Integer> entry:fileQuoteMap.entrySet()){
            if (entry.getValue().equals(0)) {
                Boolean contain = redisTemplate.opsForValue().get(UEditorFileService.UEDITOR_UPLOAD_TEMP_FILE+entry.getKey()) != null ? true : false;
                //不包含的情况
                if (!contain){
                    redisTemplate.opsForHash().delete(UEditorFileService.UEDITOR_UPLOAD_FILE, entry.getKey());
                    UEditorFileQuoteMapper.deleteByFilename(entry.getKey());
                    redisTemplate.opsForSet().remove(UEditorFileService.UEDITOR_TMEP_FILENAME_SET, entry.getKey());
                    UEditorFileService.deleteFile(entry.getKey());
                }
            }
        }
    }


}
