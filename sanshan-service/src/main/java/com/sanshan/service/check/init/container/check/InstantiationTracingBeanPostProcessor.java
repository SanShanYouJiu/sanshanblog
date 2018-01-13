package com.sanshan.service.check.init.container.check;

import com.sanshan.service.check.init.container.check.conf.loadcheck.SettingLoadCheck;
import com.sanshan.service.check.init.container.check.dataprotetcd.blogmetacache.BlogMetaDataInspect;
import com.sanshan.service.check.init.container.check.dataprotetcd.ueditorfile.UeditorFileDataInspect;
import com.sanshan.service.check.init.container.check.dataprotetcd.votecache.VoteDataInspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InstantiationTracingBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private BlogMetaDataInspect blogMetaDataInspect;

    @Autowired
    private VoteDataInspect voteDataInspect;

    @Autowired
    private UeditorFileDataInspect ueditorFileDataInspect;

    @Autowired
    private SettingLoadCheck settingLoadCheck;

    //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            blogMetaDataInspect.inspectDataConsistency();
            voteDataInspect.inspectDataConsistency();
            ueditorFileDataInspect.inspectDataConsistency();
            settingLoadCheck.loadCheck();
        }
    }

}