package com.sanshan.service.init.container.check.dataprotetcd;

import com.sanshan.dao.MarkDownBlogMapper;
import com.sanshan.dao.UEditorBlogMapper;
import com.sanshan.pojo.entity.MarkDownBlogDO;
import com.sanshan.pojo.entity.UEditorBlogDO;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.EditorTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeMap;

/**
 *可用性保证
 */
@Component
@Slf4j
public class DataBaseRollBack {

    @Autowired
    private MarkDownBlogMapper markDownBlogMapper;

    @Autowired
    private UEditorBlogMapper uEditorBlogMapper;

    @Autowired
    private BlogIdGenerate blogIdGenerate;

    //检查是否需要从数据库恢复数据
    public void inspectDataConsistency() {
        //数据库与BlogIdGenerate的事物完整性检查
        Long initTime = System.currentTimeMillis();
        log.info("进行事物一致性检查");
        List<MarkDownBlogDO> markDownBlogDOList = markDownBlogMapper.selectAll();
        List<UEditorBlogDO> uEditorBlogDOS = uEditorBlogMapper.selectAll();
        rollBackData(markDownBlogDOList, uEditorBlogDOS);
        log.info("从数据库中将恢复properties中的数据完成 耗时:{}ms", System.currentTimeMillis() - initTime);
    }

    //从数据库中将恢复properties中的数据（宕机恢复使用)
    private void rollBackData(List<MarkDownBlogDO> markDownBlogDOList, List<UEditorBlogDO> uEditorBlogDOS) {
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

        for (UEditorBlogDO u : uEditorBlogDOS) {
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
        if (idMap.size() == 0)
            return;
        Long id = idMap.lastKey();
        for (long i = 0; i < id; i++) {
            if (!idMap.containsKey(i)) {
                blogIdGenerate.addIdMap(i, EditorTypeEnum.Void_Id);
            }
        }
    }

}
