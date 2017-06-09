package com.sanshan.util;

import com.sanshan.util.exception.IdMapWriteException;
import com.sanshan.util.info.EditorTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * 组件ID生成器
 */
@Slf4j
public class BlogIdGenerate {

    //ID保存 不可轻举妄动
    private Map<Long, EditorTypeEnum> IdMap = new TreeMap<>(
            (o1, o2) -> {
                if (o1 > o2) return 1;
                else return -1;
            }
    );

    private String filename;



    public BlogIdGenerate() {

    }


    public void init() {
        try {
            IdMap = PropertiesConventMapUtil.fileToMap(filename, new TreeMap<Long, EditorTypeEnum>());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将ID推到序列中
     * @param id
     * @param type
     */
    public void addIdMap(Long id, EditorTypeEnum type) {
        IdMap.put(id, type);
    }


    /**
     * 获取新的博客的ID
     * @return 返回设置ID
     */
    public long getId() {
        return IdMap.size()+1;
    }

    public long getSize(){
        return IdMap.size();
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    public EditorTypeEnum getType(Long id) {
        return IdMap.get(id);
    }

    /**
     * 将ID在序列中移出
     * 设置为无用ID
     * @param id
     */
    public void remove(Long id) {
        IdMap.remove(id);
        IdMap.put(id, EditorTypeEnum.Void_Id);
    }

    private void printMap() {
        System.out.println(IdMap);
    }


    //定时生成IdMap文件保存到磁盘上
    private final void saveIdMap() throws IdMapWriteException {
        try {
            PropertiesConventMapUtil.MapToProperties(filename, IdMap);
        } catch (IOException e) {
            log.debug("当前的IDMap内容 {}",IdMap.toString());
            log.error("写入错误 message:{} cause:{}" ,e.getMessage() , e.getCause());
            throw new IdMapWriteException();
        }
        log.info("已将IdMap映射文件写入到 {}" , filename);
    }

}
