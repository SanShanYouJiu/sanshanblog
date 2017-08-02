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

    private final String filename;

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
        this.filename="D:\\Java3\\Github\\Module\\sanshanblog\\sanshan-util\\src\\main\\resources\\";
        log.info("加载filename值,通过该值查找相关文件");
    }


    public final void init() {
        try {
            IdMap = PropertiesConvenUtil.fileToMap(filename + "IdMap.properties", new TreeMap<Long, EditorTypeEnum>(),1);
            IdTitleMap = PropertiesConvenUtil.fileToMap(filename + "IdTitleMap.properties", new HashMap<String, Set<Long>>(),0);
            IdTagMap = PropertiesConvenUtil.fileToMap(filename + "IdTagMap.properties", new HashMap<String, Set<Long>>(),0);
            IdDateMap = PropertiesConvenUtil.fileToMap(filename + "IdDateMap.properties", new HashMap<Date, Set<Long>>(),3);
            invertTitleMap = PropertiesConvenUtil.fileToMap(filename +"invertTitleMap.properties", new HashMap<Long, String>(),2);
            invertTagMap = PropertiesConvenUtil.fileToMap(filename + "invertTagMap.properties", new HashMap<Long, String>(),2);
            invertDateMap = PropertiesConvenUtil.fileToMap(filename + "invertDateMap.properties", new HashMap<Long,Date>(),4);
            log.info("加载文件系统中的properties文件 该文件是倒排索引关键文件");
        } catch (Exception e) {
            log.error("加载失败,{}",e.getCause());
            e.printStackTrace();
        }
    }

    //ID保存 不可轻举妄动
    private  Map<Long, EditorTypeEnum> IdMap = new TreeMap<>(
            (o1, o2) -> {
                if (o1 > o2) return 1;
                else return -1;
            }
    );

    /**
     * 将ID推到序列中
     *
     * @param id
     * @param type
     */
    public final void addIdMap(Long id, EditorTypeEnum type) {
        IdMap.put(id, type);
        log.info("将Id为{}与对应的类型为{}加入到IdMap集合中",id,type);
    }


    /**
     * 获取新的博客的ID
     *
     * @return 返回设置ID
     */
    public final long getId() {
        log.info("获取当前系统新的博客Id,可能是用于新增博客");
        return IdMap.size();

    }

    public final long getSize() {
        log.info("获取当前IdMap文件长度");
        return IdMap.size();
    }


    public  final EditorTypeEnum getType(Long id) {
        log.info("获取ID为{}的博客类型",id);
        return IdMap.get(id);
    }


    /**
     * Id Title索引
     */
    private   Map<String, Set<Long>> IdTitleMap = new HashMap<>(
    );

    /**
     *已经存在Id Title索引
     */
    private Map<Long, String> invertTitleMap = new HashMap<>();

    /**
     * 上传Title索引 以及删除之前的Title索引
     */
    private String preTitle;
    public  final void putTitle(String title,Long id){
        Set<Long> longs = IdTitleMap.get(title);
        if (longs == null)
            longs = new HashSet<>();
        //查找之前id对应的key
        preTitle= invertTitleMap.get(id);
        if (!Objects.isNull(preTitle)&&preTitle!=title) {
            Set<Long> prelongs = IdTitleMap.get(preTitle);
            prelongs.remove(id);
            if (prelongs.size() == 0) {
                IdTitleMap.remove(preTitle);
            }
        }
        longs.add(id);
        IdTitleMap.put(title, longs);
        //加入到已经成为该Id对应的Title序列中
        invertTitleMap.put(id, title);
        log.info("上传标题为{},id为{}的新IdTitle索引 或许该索引项已存在 则不加",title,id);
    }

    public final Set<Long> getTitleMap(String title){
        Set<Long> longs = IdTitleMap.get(title);
        log.info("查找title为{}的Id集合",title);
        return longs;
    }

    /**
     * 拷贝发布出IdTitleMap集合中的内容
     * @return
     */
    public  final  Map<String,Set<Long>> getIdTitleCopy(){
        HashMap<String, Set<Long>> hashMap = new HashMap<>();
        for (Map.Entry<String, Set<Long>> entry : IdTitleMap.entrySet()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        log.info("拷贝发布IdTitleMap集合的内容");
        return  hashMap;
    }

    /**
     * Id tag索引
     */
    private Map<String, Set<Long>> IdTagMap = new HashMap<>(
    );
    /**
     * 已经存在的Id Tag索引
     */
    private Map<Long, String> invertTagMap = new HashMap<>();

    /**
     * 上传Tag索引以及删除之前的索引地址
     */
    private String preTag;
    public final void putTag(String tag, Long id) {
        Set<Long> longs = IdTagMap.get(tag);
        if (longs==null)
            longs = new HashSet<>();
        //查找之前id对应的key
        preTag = invertTagMap.get(id);
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
        invertTagMap.put(id, tag);
        log.info("上传标签为{},id为{}的新IdTag索引 或许该索引项已存在 则不加",tag,id);
    }

    public  final  Set<Long> getTagMap(String tag){
        Set<Long> longs = IdTagMap.get(tag);
        log.info("查找tag为{}的Id集合",tag);
        return longs;
    }

    /**
     * 拷贝发布出IdTagMap集合中的内容
     * @return
     */
    public  final  Map<String,Set<Long>> getIdTagCopy(){
        HashMap<String, Set<Long>> hashMap = new HashMap<>();
        for (Map.Entry<String, Set<Long>> entry : IdTagMap.entrySet()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        log.info("拷贝发布IdTagMap集合的内容");
        return  hashMap;
    }


    /**
     * Id Date索引
     */
    private Map<Date, Set<Long>> IdDateMap = new HashMap<>(
    );

    private Map<Long, Date> invertDateMap = new HashMap<>();

    public final void putDate(Date date, Long id) {
        Set<Long> longs = IdDateMap.get(date);
        if (longs==null)
            longs = new HashSet<>();
        longs.add(id);
        IdDateMap.put(date, longs);
        invertDateMap.put(id, date);
        log.info("上传日期为{},id为{}的新IdDate索引 或许该索引项已存在 则不加",date,id);
    }

    public  final Set<Long> getDateMap(Date date){
        Set<Long> longs = IdDateMap.get(date);
        log.info("查找日期为{}的博客",date);
        return longs;
    }

    /**
     * 拷贝发布出IdDateMap集合中的内容
     * @return
     */
    public  final  Map<Date,Set<Long>> getIdDateCopy(){
        HashMap<Date, Set<Long>> hashMap = new HashMap<>();
        for (Map.Entry<Date, Set<Long>> entry : IdDateMap.entrySet()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        log.info("拷贝发布IdDateMap集合的内容");
        return  hashMap;
    }

    /**
     * 将ID在序列中移出
     * 设置为无用ID
     *
     * @param id
     */
    public  final void remove(Long id) {
        String title = invertTitleMap.get(id);
        Set<Long> titlelongs = IdTitleMap.get(title);
        titlelongs.remove(id);
        if (titlelongs.size()==0)
            IdTitleMap.remove(title);
        invertTitleMap.remove(id);

        String tag=invertTagMap.get(id);
        Set<Long> taglongs = IdTagMap.get(tag);
        taglongs.remove(id);
        if (taglongs.size()==0)
            IdTagMap.remove(tag);
        invertTagMap.remove(id);

        Date date = invertDateMap.get(id);
        Set<Long> datelongs = IdDateMap.get(date);
        datelongs.remove(id);
        if (datelongs.size()==0)
            IdDateMap.remove(date);
        invertDateMap.remove(id);

        IdMap.put(id, EditorTypeEnum.Void_Id);
        log.info("删除该Id对应的倒排索引对应项");
    }


    //定时生成IdMap文件保存到磁盘上
    private final void saveMap() throws IdMapWriteException {
        try {
            PropertiesConvenUtil.IdMapToFile(filename + "IdMap.properties", IdMap,"其他properties的依赖文件 根据Id来判断是否查找哪种类型");
            PropertiesConvenUtil.setLongDateMapToFile(filename + "IdDateMap.properties", IdDateMap,"Id Date索引");
            PropertiesConvenUtil.setLongStringToFile(filename + "IdTagMap.properties", IdTagMap,"Id Tag索引");
            PropertiesConvenUtil.setLongStringToFile(filename + "IdTitleMap.properties", IdTitleMap,"Id Title索引");
            PropertiesConvenUtil.LongStringMapToFile(filename+"invertTitleMap.properties",invertTitleMap,"已存在的Id的title索引");
            PropertiesConvenUtil.LongStringMapToFile(filename+"invertTagMap.properties",invertTagMap,"已存在的Id的tag索引");
            PropertiesConvenUtil.LongDateMapToFile(filename+"invertDateMap.properties",invertDateMap,"已存在的Id的Date索引");
        } catch (IOException e) {
            log.error("写入错误 message:{} cause:{}", e.getMessage(), e.getCause());
            throw new IdMapWriteException();
        }
        log.info("已将Map映射文件写入到 {}", filename);
    }


}
