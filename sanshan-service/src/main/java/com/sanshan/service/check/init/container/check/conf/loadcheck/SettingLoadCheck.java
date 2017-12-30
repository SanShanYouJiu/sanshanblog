package com.sanshan.service.check.init.container.check.conf.loadcheck;

import com.alibaba.fastjson.JSON;
import com.sanshan.service.SettingService;
import com.sanshan.util.SystemUtil;
import com.sanshan.util.setting.Setting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 */
@Slf4j
@Component
public class SettingLoadCheck {

    @Autowired
    private SettingService settingService;


    @Value("${sanshanblog-setting.location}")
    private String location;


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 对加载的配置文件进行一致性检查
     */
    public void loadCheck(){
        String settingCache = (String) redisTemplate.opsForValue().get(settingService.SETTING_CACHE);
        SystemUtil systemUtil = new SystemUtil(location);
        Setting originalSetting = systemUtil.getSetting();
        if (Objects.isNull(settingCache)){
            redisTemplate.opsForValue().set(settingService.SETTING_CACHE, JSON.toJSONString(originalSetting));
            settingService.setSetting(originalSetting);
            return;
        }
        Setting setting = JSON.parseObject(settingCache,Setting.class);
        if (originalSetting.equals(setting)) {
            settingService.setSetting(setting);
        }else {
            redisTemplate.opsForValue().set(settingService.SETTING_CACHE, JSON.toJSONString(originalSetting));
            settingService.setSetting(originalSetting);
        }
        return;
    }

}
