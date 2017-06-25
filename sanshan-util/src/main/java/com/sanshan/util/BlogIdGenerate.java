package com.sanshan.util;

import com.sanshan.util.exception.IdMapWriteException;
import com.sanshan.util.info.EditorTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

/**
 * 组件ID生成器
 * 以及作为在缓存中进行查找关键字段的倒排索引的维护类
 * 生成关键文件
 */
@Slf4j
public final  class BlogIdGenerate {
    private String filename;

    //ID保存 不可轻举妄动
    private Map<Long, EditorTypeEnum> IdMap = new TreeMap<>(
            (o1, o2) -> {
                if (o1 > o2) return 1;
                else return -1;
            }
    );
    public BlogIdGenerate()  {
        //System.out.println("测试");
        //System.out.println(System.getProperty("user.dir"));
        //File file = new File("");
        //try {
        //    System.out.println(file.getCanonicalPath());
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        //System.out.println(file.getAbsolutePath());
        //TODO  这个文件路径从当前类自动获取
        filename="D:\\Java3\\Github\\Module\\sanshanblog\\sanshan-util\\src\\main\\resources\\";
    }


    public final void init() {
        try {
            IdMap = PropertiesConvenUtil.fileToMap(filename + "IdMap.properties", new TreeMap<Long, EditorTypeEnum>(),1);
            IdTitleMap = PropertiesConvenUtil.fileToMap(filename + "IdTitleMap.properties", new HashMap<String, Set<Long>>(),0);
            IdTagMap = PropertiesConvenUtil.fileToMap(filename + "IdTagMap.properties", new HashMap<String, Set<Long>>(),0);
            IdDateMap = PropertiesConvenUtil.fileToMap(filename + "IdDateMap.properties", new HashMap<Date, Set<Long>>(),3);
            preTitleMap = PropertiesConvenUtil.fileToMap(filename + "preTitleMap.properties", new HashMap<Long, String>(),2);
            preTagMap = PropertiesConvenUtil.fileToMap(filename + "preTagMap.properties", new HashMap<Long, String>(),2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Id Title索引
     */
    private Map<String, Set<Long>> IdTitleMap = new HashMap<>(
    );

    /**
     *已经存在Id Title索引
     */
    private Map<Long, String> preTitleMap = new HashMap<>();

    /**
     * 上传Title索引 以及删除之前的Title索引
     */
    private String preTitle;
    public  final void putTitle(String title,Long id){
        Set<Long> longs = IdTitleMap.get(title);
        if (longs == null)
            longs = new HashSet<>();
        //查找之前id对应的key
        preTitle= preTitleMap.get(id);
        if (!Objects.isNull(preTitle)&&preTitle!=title) {
            Set<Long> prelongs = IdTitleMap.get(preTitle);
            prelongs.remove(id);
            if (prelongs.size() == 0) {
                IdTitleMap.remove(preTitle);
            }
        }
        longs.add(id);
        IdTitleMap.put(title, longs);
        //加入到已经成为该Id对应的Ttile序列中
        preTitleMap.put(id, title);
    }

    public final Set<Long> getTitleMap(String title){
        Set<Long> longs = IdTitleMap.get(title);
        return longs;
    }

    /**
     * Id tag索引
     */
    private Map<String, Set<Long>> IdTagMap = new HashMap<>(
    );
    /**
     * 已经存在的Id Tag索引
     */
    private Map<Long, String> preTagMap = new HashMap<>();

    /**
     * 上传Tag索引以及删除之前的索引地址
     */
    private String preTag;
    public final void putTag(String tag, Long id) {
        Set<Long> longs = IdTagMap.get(tag);
        if (longs==null)
            longs = new HashSet<>();
        //查找之前id对应的key
        preTag = preTagMap.get(id);
        if (!Objects.isNull(preTag)&&preTag!=tag) {
            Set<Long> prelongs = IdTagMap.get(preTag);
            prelongs.remove(id);
            if (prelongs.size() == 0) {
                IdTagMap.remove(preTag);
            }
        }
        longs.add(id);
        IdTagMap.put(tag, longs);
        //加入到已经成为该Id对应的tag序列中
        preTagMap.put(id, tag);
    }

    public  final  Set<Long> getTagMap(String tag){
        Set<Long> longs = IdTagMap.get(tag);
        return longs;
    }

    /**
     * Id Date索引
     */
    private Map<Date, Set<Long>> IdDateMap = new HashMap<>(
    );

    public final void putDate(Date date, Long id) {
        Set<Long> longs = IdDateMap.get(date);
        if (longs==null)
            longs = new HashSet<>();
        longs.add(id);
        IdDateMap.put(date, longs);
    }

    public  final Set<Long> getDateMap(Date date){
        Set<Long> longs = IdDateMap.get(date);
        return longs;
    }



    /**
     * 将ID推到序列中
     *
     * @param id
     * @param type
     */
    public final void addIdMap(Long id, EditorTypeEnum type) {
        IdMap.put(id, type);
    }


    /**
     * 获取新的博客的ID
     *
     * @return 返回设置ID
     */
    public final long getId() {
        return IdMap.size() + 1;
    }

    public final long getSize() {
        return IdMap.size();
    }

    public  void setFilename(String filename) {
        this.filename = filename;
    }


    public  final EditorTypeEnum getType(Long id) {
        return IdMap.get(id);
    }

    /**
     * 将ID在序列中移出
     * 设置为无用ID
     *
     * @param id
     */
    public  final void remove(Long id) {
        IdMap.remove(id);
        IdMap.put(id, EditorTypeEnum.Void_Id);
    }

    private final void printMap() {
        System.out.println(IdMap);
    }


    //定时生成IdMap文件保存到磁盘上
    private final void saveMap() throws IdMapWriteException {
        try {
            PropertiesConvenUtil.IdMapToFile(filename + "IdMap.properties", IdMap,"其他properties的依赖文件 根据Id来判断是否查找哪种类型");
            PropertiesConvenUtil.setLongDateMapToFile(filename + "IdDateMap.properties", IdDateMap,"Id Date索引");
            PropertiesConvenUtil.setLongStringToFile(filename + "IdTagMap.properties", IdTagMap,"Id Tag索引");
            PropertiesConvenUtil.setLongStringToFile(filename + "IdTitleMap.properties", IdTitleMap,"Id Title索引");
            PropertiesConvenUtil.LongStringMapToFile(filename+"preTitleMap.properties",preTitleMap,"已存在的包括已失效的Id的title索引");
            PropertiesConvenUtil.LongStringMapToFile(filename+"preTagMap.properties",preTagMap,"已存在的包括已失效的Id的tag索引");
        } catch (IOException e) {
            log.error("写入错误 message:{} cause:{}", e.getMessage(), e.getCause());
            throw new IdMapWriteException();
        }
        log.info("已将Map映射文件写入到 {}", filename);
    }


}
