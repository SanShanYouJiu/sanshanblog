package com.sanshan.service.init.container.check.dataprotetcd.blogmetacache;

import com.sanshan.dao.MarkDownBlogMapper;
import com.sanshan.dao.UeditorBlogMapper;
import com.sanshan.pojo.entity.MarkDownBlogDO;
import com.sanshan.pojo.entity.UeditorBlogDO;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.EditorTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.TreeMap;

/**
 *BlogIdGenerate的properties文件的可用性保证
 */
@Component
@Slf4j
public class BlogMetaDataBaseRollBack {

    @Autowired
    private MarkDownBlogMapper markDownBlogMapper;

    @Autowired
    private UeditorBlogMapper uEditorBlogMapper;

    @Autowired
    private BlogIdGenerate blogIdGenerate;

    //检查是否需要从数据库恢复数据
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void inspectDataConsistency() {
        //数据库与BlogIdGenerate的事物完整性检查
        Long initTime = System.currentTimeMillis();
        log.info("BlogIdGenerate进行事物一致性检查");
        List<MarkDownBlogDO> markDownBlogDOList = markDownBlogMapper.selectAll();
        List<UeditorBlogDO> uEditorBlogDOS = uEditorBlogMapper.selectAll();
        rollBackData(markDownBlogDOList, uEditorBlogDOS);
        log.info("从数据库中检查properties中的数据一致性完成 耗时:{}ms", System.currentTimeMillis() - initTime);
    }

    //从数据库中回滚properties中的数据（宕机恢复使用)
    private void rollBackData(List<MarkDownBlogDO> markDownBlogDOList, List<UeditorBlogDO> uEditorBlogDOS) {
        for (MarkDownBlogDO m : markDownBlogDOList) {
            blogIdGenerate.addIdMap(m.getId(), EditorTypeEnum.MarkDown_EDITOR);
            if (m.getTitle() != null) {
                blogIdGenerate.putTitle(m.getTitle(), m.getId());
            }
            if (m.getTag() != null) {
                blogIdGenerate.putTag(m.getTag(), m.getId());
            }
            blogIdGenerate.putDate(m.getTime(), m.getId());
        }

        for (UeditorBlogDO u : uEditorBlogDOS) {
            blogIdGenerate.addIdMap(u.getId(), EditorTypeEnum.UEDITOR_EDITOR);
            if (u.getTitle() != null) {
                blogIdGenerate.putTitle(u.getTitle(), u.getId());
            }
            if (u.getTag() != null) {
                blogIdGenerate.putTag(u.getTag(), u.getId());
            }
            blogIdGenerate.putDate(u.getTime(), u.getId());
        }

        //找出被删除的ID数目 也就是void_id
        TreeMap<Long, EditorTypeEnum> idMap = (TreeMap<Long, EditorTypeEnum>) blogIdGenerate.getIdCopy();
        if (idMap.size() == 0) {
            return;
        }
        Long id = idMap.firstKey();
        for (long i = 0; i < id; i++) {
            if (!idMap.containsKey(i)) {
                blogIdGenerate.addIdMap(i, EditorTypeEnum.Void_Id);
            }
        }
    }

}
