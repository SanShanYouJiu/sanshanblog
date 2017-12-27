package com.sanshan.util;

import com.alibaba.fastjson.JSON;
import com.sanshan.util.setting.Setting;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class SystemUtil {


    public  final String filename ;

    public SystemUtil(String location) {
        filename = location;
    }

    /**
     * 得到系统配置
     * @return 结果
     */
    public   Setting getSetting() {
        //从配置文件获取
        try (InputStream in = new FileInputStream(filename))
        {
            Setting tempSetting = JSON.parseObject(in, null, Setting.class);
            log.info("get setting :{}",JSON.toJSONString(tempSetting));
            return tempSetting;
        } catch (IOException e) {
            log.error("read setting error", e);
        }
        //最后返回默认配置
        return new Setting();
    }

    /**
     * 设置或刷新系统配置
     * @param setting 设置
     */
    public  void setSetting(Setting setting){
        try {
            File file = new File(filename);
            OutputStream out = new FileOutputStream(file);
            JSON.writeJSONString(out,setting);
        } catch (IOException e) {
            log.error("read setting error",e);
        }
    }
}
