package xyz.sanshan.main.service.check.init.container.check;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.sanshan.main.service.check.init.container.check.dataprotetcd.blogmetacache.BlogMetaDataInspect;
import xyz.sanshan.main.service.check.init.container.check.dataprotetcd.ueditorfile.UeditorFileDataInspect;
import xyz.sanshan.main.service.check.init.container.check.dataprotetcd.votecache.VoteDataInspect;

@Slf4j
@Component
public class InstantiationTracingBeanPostProcessor implements CommandLineRunner {
    @Autowired
    private BlogMetaDataInspect blogMetaDataInspect;

    @Autowired
    private VoteDataInspect voteDataInspect;

    @Autowired
    private UeditorFileDataInspect ueditorFileDataInspect;

    @Override
    public void run(String... args) throws Exception {
        log.info("数据一致性检查");
        blogMetaDataInspect.inspectDataConsistency();
        voteDataInspect.inspectDataConsistency();
        ueditorFileDataInspect.inspectDataConsistency();
    }
}