package xyz.sanshan.main.service.check.init.container.check.dataprotetcd.ueditorfile;

import xyz.sanshan.main.dao.mybatis.UEditorFileQuoteMapper;
import xyz.sanshan.main.dao.mybatis.UEditorIdFileMapMapper;
import xyz.sanshan.main.pojo.entity.UEditorFileQuoteDO;
import xyz.sanshan.main.service.editor.UEditorFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 */
@Component
@Slf4j
public class UEditorFileDataInspect {


    @Autowired
    private UEditorIdFileMapMapper UEditorIdFileMapMapper;

    @Autowired
    private UEditorFileQuoteMapper UEditorFileQuoteMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     *
     从数据库恢复数据
     */
    public void inspectDataConsistency() {
        if(checkIsNeedRollback()) {
            //数据库与BlogIdGenerate的事物完整性检查
            Long initTime = System.currentTimeMillis();
            log.info("ueditor的文件数据从数据库中回滚");
            //开启事务
            SessionCallback sessionCallback = new SessionCallback() {
                @Override
                public Object execute(RedisOperations redisOperations) throws DataAccessException {
                    redisOperations.multi();
                    rollbackData();
                    return redisOperations.exec();
                }
            };
            //事务执行
            redisTemplate.execute(sessionCallback);
            log.info("ueditor上传文件数据中的数据回滚完成 耗时:{}ms", System.currentTimeMillis() - initTime);
        }
    }
    //目前缓存不需要回滚
    private Boolean checkIsNeedRollback(){
        return false;
    }


    /**
     * 数据回滚到缓存
     */
    private void rollbackData() {
        List<UEditorFileQuoteDO> fileQuoteDOS = UEditorFileQuoteMapper.selectAll();
        fileQuoteDOS.stream().forEach((fileQuoteDO)->{
            redisTemplate.opsForHash().put(UEditorFileService.UEDITOR_UPLOAD_FILE, fileQuoteDO.getFilename(), fileQuoteDO.getQuote());
        });

        List<Long> ids = UEditorIdFileMapMapper.queryByAllBlogId();
        ids.stream().forEach((id)->{
           List<String> filenames= UEditorIdFileMapMapper.queryFileNamesByBlogId(id);
            redisTemplate.opsForHash().put(UEditorFileService.UEDITOR_UPLOAD_ID_FILE_MAP, id,filenames);
        });
    }


}
