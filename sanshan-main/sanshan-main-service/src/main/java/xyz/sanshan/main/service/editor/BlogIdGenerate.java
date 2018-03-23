package xyz.sanshan.main.service.editor;

import xyz.sanshan.common.PageInfo;
import xyz.sanshan.common.PropertiesConvenUtil;
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
        Future future1 = pool.submit(() -> {
            fileToMap(filename + "idTitleMap.properties", idTitleMap, 0);
        });
        Future future2 = pool.submit(() -> {
            fileToMap(filename + "idTagMap.properties", idTagMap, 0);
        });
        Future future3 = pool.submit(() -> {
            fileToMap(filename + "idDateMap.properties", idDateMap, 3);
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

    /**
     * 删除内部维护所有数据--初始化
     * 目的是在系统重新初始化时进行使用
     * importance!!
     * danger !!
     */
    public  void initData(){
        this.invertDateMap.clear();
        this.idDateMap.clear();

        this.invertTagMap.clear();
        this.idTagMap.clear();

        this.invertTitleMap.clear();
        this.idTitleMap.clear();

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
    public synchronized final  Map<Long, EditorTypeEnum> getIdCopy() {
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
        return ((TreeMap<Long, EditorTypeEnum>) idExistMap).firstKey();
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
     * Id Title索引
     */
    private Map<String, Set<Long>> idTitleMap = new HashMap<>(
    );

    /**
     * 已经存在Id Title索引
     */
    private Map<Long, String> invertTitleMap = new HashMap<>();

    /**
     * 上传Title索引 以及删除之前的Title索引
     */

    public synchronized final   void putTitle(final String title, final Long id) {
        String preTitle;
        Set<Long> longs = idTitleMap.get(title);
        if (!Objects.isNull(longs) && longs.contains(id)) {
            return;
        }
        if (Objects.isNull(longs)){
            longs = new HashSet<>();
        }
        //查找之前id对应的key
        preTitle = invertTitleMap.get(id);
        if (!Objects.isNull(preTitle) && !preTitle.equals(title)) {
            Set<Long> prelongs = idTitleMap.get(preTitle);
            prelongs.remove(id);
            if (prelongs.size() == 0) {
                idTitleMap.remove(preTitle);
            }
        }
        longs.add(id);
        idTitleMap.put(title, longs);
        //加入到已经成为该Id对应的Title序列中
        invertTitleMap.put(id, title);
        //log.debug("上传标题为{},id为{}的新IdTitle索引 或许该索引项已存在 则不加", title, id);
    }

    public synchronized final  Set<Long> getTitleMap(final String title) {
        Set<Long> longs = idTitleMap.get(title);
        //log.debug("查找title为{}的Id集合", title);
        return longs;
    }

    /**
     * 拷贝发布出IdTitleMap集合中的内容
     *
     * @return
     */
    public synchronized final Map<String, Set<Long>> getIdTitleCopy() {
        HashMap<String, Set<Long>> copyMap = new HashMap<>();
        copyMap.putAll(idTitleMap);
        //log.debug("拷贝发布IdTitleMap集合的内容");
        return copyMap;
    }

    /**
     * 拷贝发布出InvertIdTitleMap集合中的内容
     *
     * @return
     */
    public synchronized final Map<Long, String> getInvertIdTitleMap() {
        HashMap<Long, String> copyMap = new HashMap<>();
        copyMap.putAll(invertTitleMap);
        //log.debug("拷贝发布IdTitleMap集合的内容");
        return copyMap;
    }

    public synchronized final  PageInfo getIdTitleByPage(long pageRows, long pageNum) {
        PageInfo pageInfo;
        HashMap<String, Set<Long>> copyMap = new HashMap<>();
        long preRows = pageRows * (pageNum - 1);
        long endRows = preRows + pageRows;
        long count=0;
        for (Map.Entry<String, Set<Long>> entry : idTitleMap.entrySet()) {
            if (count >= preRows && count<endRows) {
                copyMap.put(entry.getKey(), entry.getValue());
            }
            count++;
            if (count==endRows){
                pageInfo = new PageInfo(copyMap,pageRows,pageNum,idTitleMap.size());
                return pageInfo;
            }
        }
        pageInfo = new PageInfo(copyMap,pageRows,pageNum,idTitleMap.size());
        return pageInfo;
    }

    /**
     * Id tag索引
     */
    private   Map<String, Set<Long>> idTagMap = new HashMap<>(
    );
    /**
     * 已经存在的Id Tag索引
     */
    private Map<Long, String> invertTagMap = new HashMap<>();

    /**
     * 上传Tag索引以及删除之前的索引地址
     */

    public synchronized final  void putTag(final String tag, final Long id) {
        String preTag;
        Set<Long> longs = idTagMap.get(tag);
        if (!Objects.isNull(longs) && longs.contains(id)) {
            return;
        }
        if (Objects.isNull(longs)){
            longs = new HashSet<>();
        }
        //查找之前id对应的key
        preTag = invertTagMap.get(id);
        if (!Objects.isNull(preTag) && !preTag.equals(tag)) {
            Set<Long> prelongs = idTagMap.get(preTag);
            prelongs.remove(id);
            if (prelongs.size() == 0) {
                idTagMap.remove(preTag);
            }
        }
        longs.add(id);
        idTagMap.put(tag, longs);
        //加入到已经成为该Id对应的tag序列中
        invertTagMap.put(id, tag);
        //log.debug("上传标签为{},id为{}的新IdTag索引 或许该索引项已存在 则不加", tag, id);
    }

    public synchronized final Set<Long> getTagMap(final String tag) {
        Set<Long> longs = idTagMap.get(tag);
        //log.debug("查找tag为{}的Id集合", tag);
        return longs;
    }

    /**
     * 拷贝发布出IdTagMap集合中的内容
     *
     * @return
     */
    public  synchronized final Map<String, Set<Long>> getIdTagCopy() {
        HashMap<String, Set<Long>> idTagMapCopy = new HashMap<>();
        idTagMapCopy.putAll(idTagMap);
        //log.debug("拷贝发布IdTagMap集合的内容");
        return idTagMapCopy;
    }

    public synchronized final PageInfo  getIdTagCopyByPage(long pageRows,long pageNum){
        PageInfo pageInfo;
        HashMap<String,Set<Long>> copyMap = new HashMap();
        long preRows = pageRows * (pageNum - 1);
        long endRows = preRows + pageRows;
        long count=0;
        for (Map.Entry<String, Set<Long>> entry : idTagMap.entrySet()) {
            if (count >= preRows && count<endRows) {
                copyMap.put(entry.getKey(), entry.getValue());
            }
            count++;
            if (count==endRows){
                pageInfo = new PageInfo(copyMap, pageRows, pageNum, idTagMap.size());
                return pageInfo;
            }
        }
        pageInfo = new PageInfo(copyMap, pageRows, pageNum, idTagMap.size());
        return pageInfo;
    }
    /**
     * 拷贝发布出InvertTagMap集合中的内容
     */
    public synchronized final Map<Long, String> getInvertTagMap() {
        HashMap<Long, String> invertTagMapCopy = new HashMap<>();
        invertTagMapCopy.putAll(invertTagMap);
        //log.debug("拷贝发布InvertTagMap集合中的内容");
        return invertTagMapCopy;
    }


    /**
     * Id Date索引
     */
    @SuppressWarnings("Duplicates")
    private Map<Date, Set<Long>> idDateMap = new TreeMap<>(
            (o1,o2)->{
                    if (o1.equals(o2)) {
                        return 0;
                    }
                    else if (o1.before(o2)) {
                        return 1;
                    }
                    else{
                        return -1;
                    }
            }
    );

    private Map<Long, Date> invertDateMap = new HashMap<>();


    public synchronized final  void putDate(final Date date, final Long id) {
        Set<Long> longs = idDateMap.get(date);
        if (!Objects.isNull(longs) && longs.contains(id)) {
            return;
        }
        if (Objects.isNull(longs)){
            longs = new HashSet<>();
        }
        longs.add(id);
        idDateMap.put(date, longs);
        invertDateMap.put(id, date);
        //log.debug("上传日期为{},id为{}的新IdDate索引 或许该索引项已存在 则不加", date, id);
    }

    public synchronized final Set<Long> getDateMap(final Date date) {
        Set<Long> longs = idDateMap.get(date);
        //log.debug("查找日期为{}的博客", date);
        return longs;
    }

    /**
     * 拷贝发布出IdDateMap集合中的内容
     *
     * @return
     */
    @SuppressWarnings("Duplicates")
    public synchronized  final Map<Date, Set<Long>> getIdDateCopy() {
        TreeMap<Date, Set<Long>> copyMap = new TreeMap<>(
                (o1, o2) -> {
                    if (o1.equals(o2)) {
                        return 0;
                    }
                    else if (o1.before(o2)){
                        return 1;
                    }
                    else{
                        return -1;
                    }
                }
        );
        copyMap.putAll(idDateMap);
        //log.debug("拷贝发布IdDateMap集合的内容");
        return copyMap;
    }

    @SuppressWarnings("Duplicates")
    public synchronized final PageInfo getIdDateCopyByPage(long pageRows, long pageNum) {
        PageInfo pageInfo;
        TreeMap<Date, Set<Long>> copyMap = new TreeMap<>(
                (o1, o2) -> {
                    if (o1.equals(o2)) {
                        return 0;
                    }
                    else if (o1.before(o2)){
                        return 1;
                    }
                    else{
                        return -1;
                    }
                }
        );
        long preRows = pageRows * (pageNum - 1);
        long endRows = preRows + pageRows;
        long count=0;
        for (Map.Entry<Date, Set<Long>> entry : idDateMap.entrySet()) {
            if (count >= preRows && count<endRows) {
                copyMap.put(entry.getKey(), entry.getValue());
            }
            count++;
            if (count==endRows){
                pageInfo =new PageInfo(copyMap,pageRows,pageNum,idDateMap.size());
                return pageInfo;
            }
        }
        pageInfo =new PageInfo(copyMap,pageRows,pageNum,idDateMap.size());
        return pageInfo;
    }

    /**
     * 拷贝发布出InvertDateMap集合中的内容
     *
     * @return
     */
    public  synchronized final Map<Long, Date> getInvertDateMap() {
        Map<Long, Date> copyMap = new TreeMap<>();
        copyMap.putAll(invertDateMap);
        //log.debug("拷贝发布InvertDateMap集合的内容");
        return copyMap;
    }

    /**
     * 删除博客时使用
     * 设置为无用ID
     *
     * @param id
     */
    public  synchronized final void remove(final Long id) {
        String title = invertTitleMap.get(id);
        Set<Long> titlelongs = idTitleMap.get(title);
        //防止null异常
        if (!Objects.isNull(titlelongs)){
            titlelongs.remove(id);
            if (titlelongs.size() == 0){
                idTitleMap.remove(title);
            }
            invertTitleMap.remove(id);
        }

        String tag = invertTagMap.get(id);
        Set<Long> taglongs = idTagMap.get(tag);
        if (!Objects.isNull(taglongs)) {
            taglongs.remove(id);
            if (taglongs.size() == 0) {
                idTagMap.remove(tag);
            }
            invertTagMap.remove(id);
        }

        Date date = invertDateMap.get(id);
        if (!Objects.isNull(date)) {
        Set<Long> datelongs = idDateMap.get(date);
            datelongs.remove(id);
            if (datelongs.size() == 0) {
                idDateMap.remove(date);
            }
            invertDateMap.remove(id);
        }

        idMap.put(id, EditorTypeEnum.VOID_ID);
        idExistMap.remove(id);
        //log.debug("删除该Id对应的倒排索引对应项");
    }


    //定时生成IdMap文件保存到磁盘上
    private final void saveMap() {
        Long initTime = System.currentTimeMillis();
        Future future = saveMapByName("idMap.properties", "其他properties的依赖文件 根据Id来判断是否查找哪种类型");
        Future future0 = saveMapByName("idExistMap.properties", "真实存在的博客");
        Future future1 = saveMapByName("idDateMap.properties", "Id Date索引");
        Future future2 = saveMapByName("idTagMap.properties", "Id Tag索引");
        Future future3 = saveMapByName("idTitleMap.properties", "Id Title索引");
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
            case "idMap.properties":
                PropertiesConvenUtil.idMapToFile(filename + mapName, idMap, description);
                break;
            case "idExistMap.properties":
            PropertiesConvenUtil.idMapToFile(filename + mapName, idExistMap, description);
                break;
            case "idDateMap.properties":
                PropertiesConvenUtil.setLongDateMapToFile(filename + mapName, idDateMap, description);
                break;
            case "idTagMap.properties":
                PropertiesConvenUtil.setLongStringToFile(filename + mapName, idTagMap, description);
                break;
            case "idTitleMap.properties":
                PropertiesConvenUtil.setLongStringToFile(filename + mapName, idTitleMap, description);
                break;
            case "invertTitleMap.properties":
                PropertiesConvenUtil.longStringMapToFile(filename + mapName, invertTitleMap, description);
                break;
            case "invertTagMap.properties":
                PropertiesConvenUtil.longStringMapToFile(filename + mapName, invertTagMap, description);
                break;
            case "invertDateMap.properties":
                PropertiesConvenUtil.longDateMapToFile(filename + mapName, invertDateMap, description);
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
