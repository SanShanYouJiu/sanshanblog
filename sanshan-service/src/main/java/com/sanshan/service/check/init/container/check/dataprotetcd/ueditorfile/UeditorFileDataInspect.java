package com.sanshan.service.check.init.container.check.dataprotetcd.ueditorfile;

import com.sanshan.dao.UeditorFileQuoteMapper;
import com.sanshan.dao.UeditorIdFileMapMapper;
import com.sanshan.pojo.entity.UeditorFileQuoteDO;
import com.sanshan.service.editor.UeditorFileService;
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
public class UeditorFileDataInspect {


    @Autowired
    private UeditorIdFileMapMapper ueditorIdFileMapMapper;

    @Autowired
    private UeditorFileQuoteMapper ueditorFileQuoteMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     *
     从数据库恢复数据
     */
    public void inspectDataConsistency() {
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


    /**
     * 数据回滚到缓存
     */
    private void rollbackData() {
        List<UeditorFileQuoteDO> fileQuoteDOS = ueditorFileQuoteMapper.selectAll();
        fileQuoteDOS.stream().forEach((fileQuoteDO)->{
            redisTemplate.opsForHash().put(UeditorFileService.UEDITOR_UPLOAD_FILE, fileQuoteDO.getFilename(), fileQuoteDO.getQuote());
        });

        List<Long> ids = ueditorIdFileMapMapper.queryByAllBlogId();
        ids.stream().forEach((id)->{
           List<String> filenames= ueditorIdFileMapMapper.queryFileNamesByBlogId(id);
            redisTemplate.opsForHash().put(UeditorFileService.UEDITOR_UPLOAD_ID_FILE_MAP, id,filenames);
        });
    }


}
