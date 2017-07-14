package com.sanshan.service;

import com.alibaba.fastjson.JSON;
import com.sanshan.util.setting.Setting;
import com.sanshan.util.SystemUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
    public Setting getSetting(){
        String settingCache = redisTemplate.opsForValue().get(SETTING_CACHE);
        if (Objects.isNull(settingCache)){
            Setting setting = SystemUtil.getSetting();
            redisTemplate.opsForValue().set(SETTING_CACHE, JSON.toJSONString(setting));
            return setting;
        }
        Setting setting = JSON.parseObject(settingCache,Setting.class);
        if (Objects.nonNull(setting)){
            return setting;
        }
        return SystemUtil.getSetting();
    }


    public void setSetting(Setting setting){
        redisTemplate.opsForValue().set(SETTING_CACHE, String.valueOf(setting));
        SystemUtil.setSetting(setting);
    }

}
