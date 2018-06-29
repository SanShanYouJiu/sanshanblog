package xyz.sanshan.main.service.editor;

import xyz.sanshan.common.PageInfo;
import xyz.sanshan.common.util.PropertiesConvenUtil;
import xyz.sanshan.common.info.EditorTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 组件ID生成器
 * 以及作为在缓存中进行查找关键字段的倒排索引的维护类
 * 生成关键文件
 *
 * 该类属于初学代码时期的设计失误
 * TODO: 后期尽量单独维护出来 作为一个单独的组件 由组件自己去数据库同步
 */
@Slf4j
public final class BlogIdGenerate {

    private final String filename;

    final AtomicInteger poolNumber = new AtomicInteger(1);

    private ExecutorService pool = new ThreadPoolExecutor(4,9,3,TimeUnit.MINUTES,new LinkedBlockingDeque<Runnable>(),(r)->{
        Thread t = new Thread(r);
        t.setName("BlogIdGenerate-thread:"+poolNumber.incrementAndGet());
        return t;
    });

    public BlogIdGenerate( final String filename) {
        this.filename = filename;
        log.info("加载filename:{},通过该值查找相关文件",filename);
    }


    public final void init() {
        Long initTime = System.currentTimeMillis();
        Future future = pool.submit(() -> {
            fileToMap(filename + "idMap.properties", idMap, 1);
        });
        Future future0 = pool.submit(() -> {
            fileToMap(filename + "idExistMap.properties", idExistMap, 1);
        });
        getFutureResult(future);
        getFutureResult(future0);
        log.info("已加载完文件系统中的properties文件 该文件是倒排索引关键文件 耗时:{}ms", System.currentTimeMillis() - initTime);
    }

    /**
     * 删除内部维护所有数据--初始化
     * 目的是在系统重新初始化时进行使用
     *
     */
    @Deprecated
    public  void initData(){
        this.idExistMap.clear();
        this.idMap.clear();
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
    private static Map<Long, EditorTypeEnum> idMap = new TreeMap<>(
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
    private Map<Long, EditorTypeEnum> idExistMap = new TreeMap<>(
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
    public synchronized final   void addIdMap(final Long id, final EditorTypeEnum type) {
        idMap.put(id, type);
        if (!(type.equals(EditorTypeEnum.VOID_ID))){
            idExistMap.put(id, type);
        }
        //log.debug("将Id为{}与对应的类型为{}加入到IdMap集合中", id, type);
    }


    /**
     * 获取新的博客的ID
     *
     * @return 返回设置ID
     */
    public synchronized  final Long getId(EditorTypeEnum type) {
        //log.debug("获取当前系统新的博客Id,可能是用于新增博客");
        Long id;
        try {
             id = ((TreeMap<Long,EditorTypeEnum>) idMap).firstKey();
        }catch (NoSuchElementException e){
            id=0L;
        }
        //如果ID被其他博客使用了 那么就会递增到无人使用的id
        while (idMap.containsKey(id)) {
          id++;
        }
        idMap.put(id, type);
        if (!(type.equals(EditorTypeEnum.VOID_ID))) {
            idExistMap.put(id, type);
        }
        return id;
    }

    /**
     * 删除这个ID对应的IdMap
     * 可能是因为在获得ID 在之后的插入数据库失败时进行取消
     * @param id
     */
    public synchronized  final void removeIdMap(Long id){
        idMap.remove(id);
    }

    /**
     * 查看是否包含该Id
     * @param id
     * @return
     */
    public synchronized final   boolean containsId(Long id) {
        return idExistMap.containsKey(id);
    }

    /**
     * 拷贝发布IdExistMap集合内容
     *
     * @return
     */
    @SuppressWarnings("Duplicates")
    public synchronized final  TreeMap<Long, EditorTypeEnum> getIdCopy() {
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
        copyMap.putAll(idExistMap);
        //log.debug("拷贝发布IdMap集合的内容");
        return copyMap;
    }

    //根据分页查询
    @SuppressWarnings("Duplicates")
    public  synchronized final PageInfo getIdCopyByPage(long  pageRows, long pageNum){
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

        for (Map.Entry<Long, EditorTypeEnum> entry : idExistMap.entrySet()) {
            if (count >= preRows && count<endRows) {
                copyMap.put(entry.getKey(), entry.getValue());
            }
            count++;
            if (count==endRows){
                pageInfo = new PageInfo(copyMap,pageRows,pageNum,idExistMap.size());
                 return pageInfo;
            }
        }
        pageInfo = new PageInfo(copyMap,pageRows,pageNum,idExistMap.size());
        return pageInfo;
     }


    public synchronized final long getSize() {
        //log.debug("获取当前IdMap文件长度");
        return idMap.size();
    }

    public synchronized final    Long getExistMaxId(){
        //获取存活的最大ID
        Long id = null;
        try {
            id = ((TreeMap<Long, EditorTypeEnum>) idExistMap).firstKey();
        }catch (NoSuchElementException e){
            log.warn("BlogIdGenerate,idExistMap无数据");
            return null;
        }
        return id;
    }


    public synchronized final EditorTypeEnum getType(final Long id) {
        //log.debug("获取ID为{}的博客类型", id);
        if (idMap.containsKey(id)){
            return idMap.get(id);
        }else {
            return null;
        }
    }


    /**
     * 删除博客时使用
     * 设置为无用ID
     *
     * @param id
     */
    public  synchronized final void remove(final Long id) {
        idMap.put(id, EditorTypeEnum.VOID_ID);
        idExistMap.remove(id);
        //log.debug("删除该Id对应的倒排索引对应项");
    }


    //定时生成IdMap文件保存到磁盘上
    private final void saveMap() {
        Long initTime = System.currentTimeMillis();
        Future future = saveMapByName("idMap.properties", "其他properties的依赖文件 根据Id来判断是否查找哪种类型");
        Future future0 = saveMapByName("idExistMap.properties", "真实存在的博客");
        getFutureResult(future);
        getFutureResult(future0);
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
            case "idMap.properties":
                PropertiesConvenUtil.idMapToFile(filename + mapName, idMap, description);
                break;
            case "idExistMap.properties":
            PropertiesConvenUtil.idMapToFile(filename + mapName, idExistMap, description);
                break;
             default:
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
