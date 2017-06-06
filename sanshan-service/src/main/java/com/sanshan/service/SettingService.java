package com.sanshan.service;

import com.alibaba.fastjson.JSON;
import com.sanshan.pojo.entity.Setting.Setting;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sun.plugin2.util.SystemUtil;

import javax.annotation.Resource;
import java.util.Objects;

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
