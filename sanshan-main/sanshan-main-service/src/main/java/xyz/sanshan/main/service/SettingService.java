package xyz.sanshan.main.service;

import xyz.sanshan.common.SystemUtil;
import xyz.sanshan.common.setting.Setting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SettingService {

    @Value("${sanshanblog-setting.location}")
    private String location;

    private SystemUtil systemUtil =null;

    /**
     * 读取配置 缓存>配置
     * @return 系统配置
     */
    public Setting getSetting(){
        if (Objects.isNull(systemUtil)){
            systemUtil = new SystemUtil(location);
        }
        Setting setting = systemUtil.getSetting();
        return setting;
    }


    public void setSetting(Setting set){
        systemUtil.setSetting(set);
    }

}
