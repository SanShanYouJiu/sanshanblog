package com.sanshan.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SettingService {

    public static final String SETTING_CACHE = "sanshanblog-setting";


    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 读取配置 缓存>配置
     * @return 系统配置
     */
    //public Setting getSetting(){
    //    String settingCache = redisTemplate.opsForValue().get(SETTING_CACHE);
    //    if (Objects.isNull(settingCache)){
    //        return SystemUtil.getSetting();
    //    }
    //    Setting setting = JSON.parseObject(settingCache,Setting.class);
    //    if (Objects.nonNull(setting)){
    //        return setting;
    //    }
    //    return SystemUtil.getSetting();
    //}
}
