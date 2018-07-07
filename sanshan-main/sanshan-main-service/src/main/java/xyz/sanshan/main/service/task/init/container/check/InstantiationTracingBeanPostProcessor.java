package xyz.sanshan.main.service.task.init.container.check;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.sanshan.main.service.task.init.container.check.dataprotetcd.BlogMetaDataInspect;
import xyz.sanshan.main.service.task.init.container.check.dataprotetcd.UEditorFileDataInspect;
import xyz.sanshan.main.service.task.init.container.check.dataprotetcd.VoteDataInspect;

@Slf4j
@Component
public class InstantiationTracingBeanPostProcessor implements CommandLineRunner {
    @Autowired
    private BlogMetaDataInspect blogMetaDataInspect;

    @Autowired
    private VoteDataInspect voteDataInspect;

    @Autowired
    private UEditorFileDataInspect UEditorFileDataInspect;

    @Override
    public void run(String... args) throws Exception {
        log.info("数据一致性检查 -> 从数据库恢复数据");
        //不推荐开启 如果出错的情况可以在项目启动时开启作为一次数据恢复
        blogMetaDataInspect.inspectDataConsistency();
        voteDataInspect.inspectDataConsistency();
        UEditorFileDataInspect.inspectDataConsistency();
    }
}