package xyz.sanshan.service.check.night;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import xyz.sanshan.dao.mybatis.UeditorFileQuoteMapper;
import xyz.sanshan.service.editor.UeditorFileService;

import java.util.Map;

/**
 */
@Component
@Slf4j
public class UeditorFileUploadCheck {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UeditorFileService ueditorFileService;

    @Autowired
    private UeditorFileQuoteMapper ueditorFileQuoteMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void check(){
       Map<String,Integer> fileQuoteMap= redisTemplate.opsForHash().entries(UeditorFileService.UEDITOR_UPLOAD_FILE);
       //首先检查引用为0的 然后查看引用0的在UEDITOR_UPLOAD_TEMP_FILE缓存中是否存在
        for(Map.Entry<String,Integer> entry:fileQuoteMap.entrySet()){
            if (entry.getValue().equals(0)) {
                Boolean contain = redisTemplate.opsForValue().get(UeditorFileService.UEDITOR_UPLOAD_TEMP_FILE+entry.getKey()) != null ? true : false;
                //不包含的情况
                if (!contain){
                    redisTemplate.opsForHash().delete(UeditorFileService.UEDITOR_UPLOAD_FILE, entry.getKey());
                    ueditorFileQuoteMapper.deleteByFilename(entry.getKey());
                    redisTemplate.opsForSet().remove(UeditorFileService.UEDITOR_TMEP_FILENAME_SET, entry.getKey());
                    ueditorFileService.deleteFile(entry.getKey());
                }
            }
        }
    }


}
