package com.sanshan.service;

import com.sanshan.util.SystemUtil;
import com.sanshan.util.setting.Setting;
import org.springframework.stereotype.Service;

@Service
public class SettingService {

    public static final String SETTING_CACHE = "sanshanblog-setting";

    //这里的setting是在初始化检查时赋值的
    public static Setting setting;

    /**
     * 读取配置 缓存>配置
     * @return 系统配置
     */
    public Setting getSetting(){
        return setting;
    }


    public void setSetting(Setting set){
        setting=set;
        SystemUtil.setSetting(set);
    }

}
