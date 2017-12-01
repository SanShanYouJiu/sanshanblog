package com.sanshan.util;

import com.sanshan.util.info.EditorTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 组件ID生成器
 * 以及作为在缓存中进行查找关键字段的倒排索引的维护类
 * 生成关键文件
 */
@Slf4j
public final class BlogIdGenerate {

    private final String filename;

    final AtomicInteger poolNumber = new AtomicInteger(1);

    private ExecutorService pool = new ThreadPoolExecutor(4,9,1,TimeUnit.MINUTES,new LinkedBlockingDeque<Runnable>(),(r)->{
        Thread t = new Thread(r);
        t.setName("BlogIdGenerate-thread:"+poolNumber.incrementAndGet());
        return t;
    });

    public BlogIdGenerate( final String filename) {
        this.filename = filename;
        log.info("加载filename值,通过该值查找相关文件");
    }


    public final void init() {
        Long initTime = System.currentTimeMillis();
        Future future = pool.submit(() -> {
            fileToMap(filename + "IdMap.properties", IdMap, 1);
        });
        Future future0 = pool.submit(() -> {
            fileToMap(filename + "IdExistMap.properties", IdExistMap, 1);
        });
        Future future1 = pool.submit(() -> {
            fileToMap(filename + "IdTitleMap.properties", IdTitleMap, 0);
        });
        Future future2 = pool.submit(() -> {
            fileToMap(filename + "IdTagMap.properties", IdTagMap, 0);
        });
        Future future3 = pool.submit(() -> {
            fileToMap(filename + "IdDateMap.properties", IdDateMap, 3);
        });
        Future future4 = pool.submit(() -> {
            fileToMap(filename + "invertTitleMap.properties", invertTitleMap, 2);
        });
        Future future5 = pool.submit(() -> {
            fileToMap(filename + "invertTagMap.properties", invertTagMap, 2);
        });
        Future future6 = pool.submit(() -> {
            fileToMap(filename + "invertDateMap.properties", invertDateMap, 4);
        });
        getFutureResult(future);
        getFutureResult(future0);
        getFutureResult(future1);
        getFutureResult(future2);
        getFutureResult(future3);
        getFutureResult(future4);
        getFutureResult(future5);
        getFutureResult(future6);
        log.info("已加载完文件系统中的properties文件 该文件是倒排索引关键文件 耗时:{}ms", System.currentTimeMillis() - initTime);
    }

    private void fileToMap(String filename, Map map, int type) {
        try {
            PropertiesConvenUtil.fileToMap(filename, map, type);
        } catch (Exception e) {
            log.error("加载文件:{}失败,cause:{}", e.getCause());
            e.printStackTrace();
        }
    }


    //ID保存 不可轻举妄动
    @SuppressWarnings("Duplicates")
    private Map<Long, EditorTypeEnum> IdMap = new TreeMap<>(
            (o1, o2) -> {
                if (o1.equals(o2)) {
                    return 0;
                } else if (o1> o2) {
                    return -1;
                } else {
                    return 1;
                }
            }
    );

    @SuppressWarnings("Duplicates")
    private Map<Long, EditorTypeEnum> IdExistMap = new TreeMap<>(
            (o1, o2) -> {
                if (o1.equals(o2)) {
                    return 0;
                } else if (o1> o2) {
                    return -1;
                } else {
                    return 1;
                }
            }
    );

    /**
     * 将ID推到序列中
     *
     * @param id
     * @param type
     */
    public final void addIdMap(final Long id, final EditorTypeEnum type) {
        IdMap.put(id, type);
        if (!(type==EditorTypeEnum.Void_Id))
           IdExistMap.put(id, type);
        log.debug("将Id为{}与对应的类型为{}加入到IdMap集合中", id, type);
    }


    /**
     * 获取新的博客的ID
     *
     * @return 返回设置ID
     */
    public final long getId() {
        log.debug("获取当前系统新的博客Id,可能是用于新增博客");
        return IdMap.size();

    }

    /**
     * 查看是否包含该Id
     * @param id
     * @return
     */
    public final boolean containsId(Long id) {
        return IdExistMap.containsKey(id);
    }

    /**
     * 拷贝发布IdExistMap集合内容
     *
     * @return
     */
    @SuppressWarnings("Duplicates")
    public final Map<Long, EditorTypeEnum> getIdCopy() {
        TreeMap<Long, EditorTypeEnum> copyMap = new TreeMap<>(
                (o1, o2) -> {
                    if (o1.equals(o2)) {
                        return 0;
                    } else if (o1> o2) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
        );
        copyMap.putAll(IdExistMap);
        log.debug("拷贝发布IdMap集合的内容");
        return copyMap;
    }

    //根据分页查询
    @SuppressWarnings("Duplicates")
    public final PageInfo getIdCopyByPage(long  pageRows,long pageNum){
        PageInfo pageInfo;
        TreeMap<Long, EditorTypeEnum> copyMap = new TreeMap<>(
                 (o1, o2) -> {
                     if (o1.equals(o2)) {
                         return 0;
                     } else if (o1> o2) {
                         return -1;
                     } else {
                         return 1;
                     }
                 }
         );
        long preRows = pageRows * (pageNum - 1);
        long endRows = preRows + pageRows;
        long count=0;

        for (Map.Entry<Long, EditorTypeEnum> entry : IdExistMap.entrySet()) {
            if (count >= preRows && count<endRows) {
                copyMap.put(entry.getKey(), entry.getValue());
            }
            count++;
            if (count==endRows){
                pageInfo = new PageInfo(copyMap,pageRows,pageNum,IdExistMap.size());
                 return pageInfo;
            }
        }
        pageInfo = new PageInfo(copyMap,pageRows,pageNum,IdExistMap.size());
        return pageInfo;
     }


    public final long getSize() {
        log.debug("获取当前IdMap文件长度");
        return IdMap.size();
    }


    public final EditorTypeEnum getType(final Long id) {
        log.debug("获取ID为{}的博客类型", id);
        if (IdMap.containsKey(id)){
            return IdMap.get(id);
        }else {
            return null;
        }
    }


    /**
     * Id Title索引
     */
    private Map<String, Set<Long>> IdTitleMap = new HashMap<>(
    );

    /**
     * 已经存在Id Title索引
     */
    private Map<Long, String> invertTitleMap = new HashMap<>();

    /**
     * 上传Title索引 以及删除之前的Title索引
     */
    private String preTitle;

    public final void putTitle(final String title, final Long id) {
        Set<Long> longs = IdTitleMap.get(title);
        if (longs != null && longs.contains(id)) {
            return;
        }
        if (longs == null)
            longs = new HashSet<>();
        //查找之前id对应的key
        preTitle = invertTitleMap.get(id);
        if (!Objects.isNull(preTitle) && preTitle != title) {
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
        log.debug("上传标题为{},id为{}的新IdTitle索引 或许该索引项已存在 则不加", title, id);
    }

    public final Set<Long> getTitleMap(final String title) {
        Set<Long> longs = IdTitleMap.get(title);
        log.debug("查找title为{}的Id集合", title);
        return longs;
    }

    /**
     * 拷贝发布出IdTitleMap集合中的内容
     *
     * @return
     */
    public final Map<String, Set<Long>> getIdTitleCopy() {
        HashMap<String, Set<Long>> copyMap = new HashMap<>();
        copyMap.putAll(IdTitleMap);
        log.debug("拷贝发布IdTitleMap集合的内容");
        return copyMap;
    }

    /**
     * 拷贝发布出InvertIdTitleMap集合中的内容
     *
     * @return
     */
    public final Map<Long, String> getInvertIdTitleMap() {
        HashMap<Long, String> copyMap = new HashMap<>();
        copyMap.putAll(invertTitleMap);
        log.debug("拷贝发布IdTitleMap集合的内容");
        return copyMap;
    }

    public final  PageInfo getIdTitleByPage(long pageRows, long pageNum) {
        PageInfo pageInfo;
        HashMap<String, Set<Long>> copyMap = new HashMap<>();
        long preRows = pageRows * (pageNum - 1);
        long endRows = preRows + pageRows;
        long count=0;
        for (Map.Entry<String, Set<Long>> entry : IdTitleMap.entrySet()) {
            if (count >= preRows && count<endRows) {
                copyMap.put(entry.getKey(), entry.getValue());
            }
            count++;
            if (count==endRows){
                pageInfo = new PageInfo(copyMap,pageRows,pageNum,IdTitleMap.size());
                return pageInfo;
            }
        }
        pageInfo = new PageInfo(copyMap,pageRows,pageNum,IdTitleMap.size());
        return pageInfo;
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

    public final void putTag(final String tag, final Long id) {
        Set<Long> longs = IdTagMap.get(tag);
        if (longs != null && longs.contains(id)) {
            return;
        }
        if (longs == null)
            longs = new HashSet<>();
        //查找之前id对应的key
        preTag = invertTagMap.get(id);
        if (!Objects.isNull(preTag) && preTag != tag) {
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
        log.debug("上传标签为{},id为{}的新IdTag索引 或许该索引项已存在 则不加", tag, id);
    }

    public final Set<Long> getTagMap(final String tag) {
        Set<Long> longs = IdTagMap.get(tag);
        log.debug("查找tag为{}的Id集合", tag);
        return longs;
    }

    /**
     * 拷贝发布出IdTagMap集合中的内容
     *
     * @return
     */
    public final Map<String, Set<Long>> getIdTagCopy() {
        HashMap<String, Set<Long>> idTagMapCopy = new HashMap<>();
        idTagMapCopy.putAll(IdTagMap);
        log.debug("拷贝发布IdTagMap集合的内容");
        return idTagMapCopy;
    }

    public final PageInfo  getIdTagCopyByPage(long pageRows,long pageNum){
        PageInfo pageInfo;
        HashMap<String,Set<Long>> copyMap = new HashMap();
        long preRows = pageRows * (pageNum - 1);
        long endRows = preRows + pageRows;
        long count=0;
        for (Map.Entry<String, Set<Long>> entry : IdTagMap.entrySet()) {
            if (count >= preRows && count<endRows) {
                copyMap.put(entry.getKey(), entry.getValue());
            }
            count++;
            if (count==endRows){
                pageInfo = new PageInfo(copyMap, pageRows, pageNum, IdTagMap.size());
                return pageInfo;
            }
        }
        pageInfo = new PageInfo(copyMap, pageRows, pageNum, IdTagMap.size());
        return pageInfo;
    }
    /**
     * 拷贝发布出InvertTagMap集合中的内容
     */
    public final Map<Long, String> getInvertTagMap() {
        HashMap<Long, String> invertTagMapCopy = new HashMap<>();
        invertTagMapCopy.putAll(invertTagMap);
        log.debug("拷贝发布InvertTagMap集合中的内容");
        return invertTagMapCopy;
    }


    /**
     * Id Date索引
     */
    @SuppressWarnings("Duplicates")
    private Map<Date, Set<Long>> IdDateMap = new TreeMap<>(
            (o1,o2)->{
                    if (o1.equals(o2)) return 0;
                    else if (o1.before(o2)) return 1;
                    else return -1;
            }
    );

    private Map<Long, Date> invertDateMap = new HashMap<>();


    public final void putDate(final Date date, final Long id) {
        Set<Long> longs = IdDateMap.get(date);
        if (longs != null && longs.contains(id)) {
            return;
        }
        if (longs == null)
            longs = new HashSet<>();
        longs.add(id);
        IdDateMap.put(date, longs);
        invertDateMap.put(id, date);
        log.debug("上传日期为{},id为{}的新IdDate索引 或许该索引项已存在 则不加", date, id);
    }

    public final Set<Long> getDateMap(final Date date) {
        Set<Long> longs = IdDateMap.get(date);
        log.debug("查找日期为{}的博客", date);
        return longs;
    }

    /**
     * 拷贝发布出IdDateMap集合中的内容
     *
     * @return
     */
    @SuppressWarnings("Duplicates")
    public final Map<Date, Set<Long>> getIdDateCopy() {
        TreeMap<Date, Set<Long>> copyMap = new TreeMap<>(
                (o1, o2) -> {
                    if (o1.equals(o2)) return 0;
                    else if (o1.before(o2)) return 1;
                    else return -1;
                }
        );
        copyMap.putAll(IdDateMap);
        log.debug("拷贝发布IdDateMap集合的内容");
        return copyMap;
    }

    @SuppressWarnings("Duplicates")
    public final PageInfo getIdDateCopyByPage(long pageRows, long pageNum) {
        PageInfo pageInfo;
        TreeMap<Date, Set<Long>> copyMap = new TreeMap<>(
                (o1, o2) -> {
                    if (o1.equals(o2)) return 0;
                    else if (o1.before(o2)) return 1;
                    else return -1;
                }
        );
        long preRows = pageRows * (pageNum - 1);
        long endRows = preRows + pageRows;
        long count=0;
        for (Map.Entry<Date, Set<Long>> entry : IdDateMap.entrySet()) {
            if (count >= preRows && count<endRows) {
                copyMap.put(entry.getKey(), entry.getValue());
            }
            count++;
            if (count==endRows){
                pageInfo =new PageInfo(copyMap,pageRows,pageNum,IdDateMap.size());
                return pageInfo;
            }
        }
        pageInfo =new PageInfo(copyMap,pageRows,pageNum,IdDateMap.size());
        return pageInfo;
    }

    /**
     * 拷贝发布出InvertDateMap集合中的内容
     *
     * @return
     */
    public final Map<Long, Date> getInvertDateMap() {
        Map<Long, Date> copyMap = new TreeMap<>();
        copyMap.putAll(invertDateMap);
        log.debug("拷贝发布InvertDateMap集合的内容");
        return copyMap;
    }

    /**
     * 将ID在序列中移出
     * 设置为无用ID
     *
     * @param id
     */
    public final void remove(final Long id) {
        String title = invertTitleMap.get(id);
        Set<Long> titlelongs = IdTitleMap.get(title);
        titlelongs.remove(id);
        if (titlelongs.size() == 0)
            IdTitleMap.remove(title);
        invertTitleMap.remove(id);

        String tag = invertTagMap.get(id);
        Set<Long> taglongs = IdTagMap.get(tag);
        taglongs.remove(id);
        if (taglongs.size() == 0)
            IdTagMap.remove(tag);
        invertTagMap.remove(id);

        Date date = invertDateMap.get(id);
        Set<Long> datelongs = IdDateMap.get(date);
        datelongs.remove(id);
        if (datelongs.size() == 0)
            IdDateMap.remove(date);
        invertDateMap.remove(id);

        IdMap.put(id, EditorTypeEnum.Void_Id);
        IdExistMap.remove(id);
        log.debug("删除该Id对应的倒排索引对应项");
    }


    //定时生成IdMap文件保存到磁盘上
    private final void saveMap() {
        Long initTime = System.currentTimeMillis();
        Future future = saveMapByName("IdMap.properties", "其他properties的依赖文件 根据Id来判断是否查找哪种类型");
        Future future0 = saveMapByName("IdExistMap.properties", "真实存在的博客");
        Future future1 = saveMapByName("IdDateMap.properties", "Id Date索引");
        Future future2 = saveMapByName("IdTagMap.properties", "Id Tag索引");
        Future future3 = saveMapByName("IdTitleMap.properties", "Id Title索引");
        Future future4 = saveMapByName("invertTitleMap.properties", "已存在的Id的title索引2");
        Future future5 = saveMapByName("invertTagMap.properties", "已存在的Id的tag索引");
        Future future6 = saveMapByName("invertDateMap.properties", "已存在的Id的Date索引");
        getFutureResult(future);
        getFutureResult(future0);
        getFutureResult(future1);
        getFutureResult(future2);
        getFutureResult(future3);
        getFutureResult(future4);
        getFutureResult(future5);
        getFutureResult(future6);
        log.debug("已刷新最新内存中的BlogIdGenerate数据到磁盘中 耗时:{}ms", System.currentTimeMillis() - initTime);
    }


    private Future saveMapByName(final String mapName, final String description) {
        Runnable r = () -> {
            try {
                switchTypePropertiesToFile(mapName, description);
            } catch (IOException e) {
                log.error("{}文件写入错误", mapName);
                e.printStackTrace();
            }
            log.debug("已将Map:{}映射文件写入到 {}下", mapName, filename);
        };
        return pool.submit(r);
    }

    private void switchTypePropertiesToFile(final String mapName, final String description) throws IOException {
        switch (mapName) {
            case "IdMap.properties":
                PropertiesConvenUtil.IdMapToFile(filename + mapName, IdMap, description);
                break;
            case "IdExistMap.properties":
            PropertiesConvenUtil.IdMapToFile(filename + mapName, IdExistMap, description);
                break;
            case "IdDateMap.properties":
                PropertiesConvenUtil.setLongDateMapToFile(filename + mapName, IdDateMap, description);
                break;
            case "IdTagMap.properties":
                PropertiesConvenUtil.setLongStringToFile(filename + mapName, IdTagMap, description);
                break;
            case "IdTitleMap.properties":
                PropertiesConvenUtil.setLongStringToFile(filename + mapName, IdTitleMap, description);
                break;
            case "invertTitleMap.properties":
                PropertiesConvenUtil.LongStringMapToFile(filename + mapName, invertTitleMap, description);
                break;
            case "invertTagMap.properties":
                PropertiesConvenUtil.LongStringMapToFile(filename + mapName, invertTagMap, description);
                break;
            case "invertDateMap.properties":
                PropertiesConvenUtil.LongDateMapToFile(filename + mapName, invertDateMap, description);
                break;
        }
    }

    private void getFutureResult(Future future) {
        try {
            future.get();
        } catch (InterruptedException e) {
            log.error("出现中断错误:{}", e.getMessage());
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
