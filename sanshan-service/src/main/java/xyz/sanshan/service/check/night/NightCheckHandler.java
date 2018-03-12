package xyz.sanshan.service.check.night;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 午夜检查相关信息是否成功核对
 * @author sanshan
 */
@Service
@Slf4j
public class NightCheckHandler {

    @Autowired
    private UeditorFileUploadCheck ueditorFileUploadCheck;

    public void nightCheck() {
        log.info("午夜检测开始");
        ueditorFileUploadCheck.check();
        log.info("核对完毕");
    }
}
