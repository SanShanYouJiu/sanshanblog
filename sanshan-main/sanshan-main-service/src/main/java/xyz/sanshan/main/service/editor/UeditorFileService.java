package xyz.sanshan.main.service.editor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.sanshan.main.pojo.dto.UeditorFileQuoteDTO;
import xyz.sanshan.main.pojo.dto.UeditorIdFileMapDTO;
import xyz.sanshan.main.service.upload.QiniuStorageManager;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 这里一旦发生Redis的缓存不可用 就会从数据库中加载 但是暂存文件的数据并不会从数据库中恢复
 * <{@code UEDITOR_UPLOAD_TEMP_FILE}缓存与 <{@code UEDITOR_TMEP_FILENAME_SET}缓存没有在数据库中
 */
@Slf4j
@Service
public class UeditorFileService {


    @Autowired
    private QiniuStorageManager qiniuStorageManager;

    @Autowired
    private RedisTemplate redisTemplate;

    public static  final String UEDITOR_UPLOAD_TEMP_FILE="ueditor_upload:tmep_file:";

    public static final String UEDITOR_TMEP_FILENAME_SET = "ueditor_upload:temp_file_set:";

    public  static  final  String UEDITOR_UPLOAD_FILE="ueditor_upload:file:";

    public static final String UEDITOR_UPLOAD_ID_FILE_MAP="ueditor_upload:id_file_map:";

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

    private ExecutorService pool = new ThreadPoolExecutor(0, 4,
            10, TimeUnit.MINUTES,
            new SynchronousQueue<Runnable>(),(r)->{
        Thread t = new Thread(r);
        t.setName("ueditor-Checkfile-thread:"+POOL_NUMBER.incrementAndGet());
        return t;
    });


    public static ConcurrentLinkedQueue<UeditorIdFileMapDTO> ueditorFileAddQueue = new ConcurrentLinkedQueue<>();

    public static ConcurrentLinkedQueue<UeditorFileQuoteDTO> ueditorFileUpload = new ConcurrentLinkedQueue<>();

    public static ConcurrentLinkedQueue<UeditorIdFileMapDTO> ueditorFileDecrQueue = new ConcurrentLinkedQueue<>();

    /**
     *  存入图片到七牛云
     *
     * Redis只能保证基本可用
     * @param content  文件
     * @param filename 文件名
     * @param type 类型
     * @param metedata 元数据 元数据其实就是数据的数据 其他附带的一些信息
     */
    public void saveFile(InputStream content, String filename, String type, Object metedata) {
        qiniuStorageManager.ueditorUpload(content, filename,type,metedata);
        //上传过的暂时文件 12小时候后从这个缓存中消失 代表这个暂存文件只存在12小时
        //实际上 如果客户运气足够好的话 这个图片可以存在36小时 因为一天更新比对一次
        redisTemplate.opsForValue().set(UEDITOR_UPLOAD_TEMP_FILE+filename,filename,12, TimeUnit.HOURS);
        //将暂存文件名存入到一个专门的Set中
        redisTemplate.opsForSet().add(UEDITOR_TMEP_FILENAME_SET,filename);
        //上传过文件的列表 一天更新一次 在晚上1点凌晨进行比对
        // 如果是在UEDITOR_UPLOAD_TEMP_FILE缓存没有 这一个缓存中有的文件就进行审核 值为0的代表0人引用 需要将这条缓存中以及暂存文件名的set中也删除
        redisTemplate.opsForHash().put(UEDITOR_UPLOAD_FILE, filename, 0);
        //加入到数据库中
        UeditorFileQuoteDTO ueditorFileQuoteDTO = new UeditorFileQuoteDTO(filename,0);
        ueditorFileUpload.add(ueditorFileQuoteDTO);
    }

    /**
     * 检测Ueditor内容中的文件 并且提交到缓存中作记录
     *
     * @param id      Ueditor的博客ID
     * @param content 文件
     */
    protected void checkUeditorContentFile(Long id, String content) {
        log.info("检查id为{}的ueditor博客含有的文件内容",id);
        pool.execute(() -> {
            Set<String> filenameSet = redisTemplate.opsForSet().members(UEDITOR_TMEP_FILENAME_SET);
            String[] filenames = filenameSet.toArray(new String[]{});
            List<String> blogFiles = new LinkedList<>();
            for (int i = 0; i < filenames.length; i++) {
                String filename= filenames[i];
                Boolean contain = StringUtils.contains(content, filename);
                if (contain) {
                    //增加一个博客的引用
                    redisTemplate.opsForHash().increment(UEDITOR_UPLOAD_FILE, filename, 1);
                    redisTemplate.opsForSet().remove(UEDITOR_TMEP_FILENAME_SET, filename);
                    blogFiles.add(filename);
                }
            }
            //在ID与文件对应表中进行关联
            redisTemplate.opsForHash().put(UEDITOR_UPLOAD_ID_FILE_MAP, id, blogFiles);
            //放到consumer中 定时进入数据库存储
            UeditorIdFileMapDTO idFileMapDTO = new UeditorIdFileMapDTO(id,blogFiles);
            ueditorFileAddQueue.add(idFileMapDTO);
        });
    }

    /**
     * 删除博客ID对应表 并且减少文件的引用数
     * @param blogId
     */
    public void deleteContentContainsFile(Long blogId) {
        log.info("删除id为{}的ueditor博客中在本站含有的文件",blogId);
        pool.execute(()->{
            List<String> filenames;
            filenames = (List<String>) redisTemplate.opsForHash().get(UEDITOR_UPLOAD_ID_FILE_MAP, blogId);
            //没有文件退出方法
            if(Objects.isNull(filenames)){
                return;
            }
            for (int i = 0; i <filenames.size() ; i++) {
                String filename = filenames.get(i);
                redisTemplate.opsForHash().increment(UEDITOR_UPLOAD_FILE, filename, -1);
            }
            //删除该博客ID文件对应表
            redisTemplate.opsForHash().delete(UEDITOR_UPLOAD_ID_FILE_MAP, blogId);
            UeditorIdFileMapDTO ueditorIdFileMapDTO = new UeditorIdFileMapDTO(blogId,filenames);
            ueditorFileDecrQueue.add(ueditorIdFileMapDTO);
        });
    }


    /**
     * 删除图片
     * @param filename 文件名
     */
    public void deleteFile(String filename){
        qiniuStorageManager.ueditorDeleteFile(filename);
    }


}
