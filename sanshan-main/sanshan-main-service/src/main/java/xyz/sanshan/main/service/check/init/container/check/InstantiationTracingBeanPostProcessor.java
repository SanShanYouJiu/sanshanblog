package xyz.sanshan.main.service.check.init.container.check;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import xyz.sanshan.main.service.check.init.container.check.dataprotetcd.blogmetacache.BlogMetaDataInspect;
import xyz.sanshan.main.service.check.init.container.check.dataprotetcd.ueditorfile.UeditorFileDataInspect;
import xyz.sanshan.main.service.check.init.container.check.dataprotetcd.votecache.VoteDataInspect;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class InstantiationTracingBeanPostProcessor implements ApplicationListener<ApplicationPreparedEvent> {
    private AtomicInteger count = new AtomicInteger(0);
    @Autowired
    private BlogMetaDataInspect blogMetaDataInspect;

    @Autowired
    private VoteDataInspect voteDataInspect;

    @Autowired
    private UeditorFileDataInspect ueditorFileDataInspect;


    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        if (count.incrementAndGet()==1) {
            //FIXME 存在两个相同的上下文
            log.info("数据一致性检查");
            blogMetaDataInspect.inspectDataConsistency();
            voteDataInspect.inspectDataConsistency();
            ueditorFileDataInspect.inspectDataConsistency();
        }
    }
}