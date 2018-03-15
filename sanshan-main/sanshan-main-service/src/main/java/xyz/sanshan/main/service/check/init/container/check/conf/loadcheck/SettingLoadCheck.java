package xyz.sanshan.main.service.check.init.container.check.conf.loadcheck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 */
@Slf4j
@Component
public class SettingLoadCheck {

    //@Autowired
    //private SettingService settingService;

    //@Autowired
    //private RedisTemplate redisTemplate;

    /**
     * 对加载的配置文件进行一致性检查
     *
     * 废弃
     */
    @Deprecated
    public void loadCheck(){
        //String settingCache = (String) redisTemplate.opsForValue().get(SettingService.SETTING_CACHE);
        //SystemUtil systemUtil = new SystemUtil(location);
        //Setting originalSetting = systemUtil.getSetting();
        //if (Objects.isNull(settingCache)){
        //    redisTemplate.opsForValue().set(settingService.SETTING_CACHE, JSON.toJSONString(originalSetting));
        //    settingService.setSetting(originalSetting);
        //    return;
        //}
        //Setting setting = JSON.parseObject(settingCache,Setting.class);
        //if (originalSetting.equals(setting)) {
        //    settingService.setSetting(setting);
        //}else {
        //    redisTemplate.opsForValue().set(settingService.SETTING_CACHE, JSON.toJSONString(originalSetting));
        //    settingService.setSetting(originalSetting);
        //}
        //return;
    }

}
