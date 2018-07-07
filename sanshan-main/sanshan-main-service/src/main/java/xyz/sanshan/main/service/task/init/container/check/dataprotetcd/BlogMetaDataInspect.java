package xyz.sanshan.main.service.task.init.container.check.dataprotetcd;

import xyz.sanshan.main.dao.mybatis.MarkDownBlogMapper;
import xyz.sanshan.main.dao.mybatis.UEditorBlogMapper;
import xyz.sanshan.main.pojo.entity.MarkDownBlogDO;
import xyz.sanshan.main.pojo.entity.UEditorBlogDO;
import xyz.sanshan.main.service.editor.BlogIdGenerate;
import xyz.sanshan.common.info.EditorTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

/**
 *BlogIdGenerate的properties文件的可用性保证
 */
@Component
@Slf4j
public class BlogMetaDataInspect {

    @Autowired
    private MarkDownBlogMapper markDownBlogMapper;

    @Autowired
    private UEditorBlogMapper uEditorBlogMapper;

    @Autowired
    private BlogIdGenerate blogIdGenerate;

    /**
     *
     从数据库回滚数据
     */
    public void inspectDataConsistency() {
        if (checkIsEmpty()) {
            //数据库与BlogIdGenerate的事物完整性检查
            Long initTime = System.currentTimeMillis();
            log.info("BlogIdGenerate从数据库中回滚数据");
            //初始化数据
            blogIdGenerate.initData();
            List<MarkDownBlogDO> markDownBlogDOList = markDownBlogMapper.selectAll();
            List<UEditorBlogDO> uEditorBlogDOS = uEditorBlogMapper.selectAll();
            rollBackData(markDownBlogDOList, uEditorBlogDOS);
            log.info("从数据库中回滚properties中的数据完成 耗时:{}ms", System.currentTimeMillis() - initTime);
        }else {
            //策略：
            // 默认 认为文件内容可靠 不进行回滚
            // 如果是外部配置也可以认为不需要回滚 但是这个配置不是默认选项
        }
    }

    /**
     * 检测文件内容是否为空
     */
    private Boolean checkIsEmpty(){
       if (Objects.isNull(blogIdGenerate.getExistMaxId())){
           return true;
       }
        return false;
    }

    /**
     从数据库中回滚properties中的数据（宕机恢复使用)
     */
    private void rollBackData(List<MarkDownBlogDO> markDownBlogDOList, List<UEditorBlogDO> uEditorBlogDOS) {
        for (MarkDownBlogDO m : markDownBlogDOList) {
            blogIdGenerate.addIdMap(m.getId(), EditorTypeEnum.MARKDOWN_EDITOR);
        }

        for (UEditorBlogDO u : uEditorBlogDOS) {
            blogIdGenerate.addIdMap(u.getId(), EditorTypeEnum.UEDITOR_EDITOR);
        }

        //找出被删除的ID数目 也就是void_id
        TreeMap<Long, EditorTypeEnum> idMap =  blogIdGenerate.getIdCopy();
        if (idMap.size() == 0) {
            return;
        }
        Long id = idMap.firstKey();
        for (long i = 0; i < id; i++) {
            if (!idMap.containsKey(i)) {
                blogIdGenerate.addIdMap(i, EditorTypeEnum.VOID_ID);
            }
        }
    }

}
