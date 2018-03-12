package xyz.sanshan.service.upload;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * 七牛云存储相关
 */
@Service
@Slf4j
public class QiniuStorageManager {


    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.ueditor-bucket}")
    private String ueditor_bucket;

    /**
     * 七牛云的配置选项
     * Zone.zone2为华南的节点
     */
    private Configuration cfg = new Configuration(Zone.zone2());

    private UploadManager uploadManager = new UploadManager(cfg);


    /**
     * 上传Ueditor的文件
     * @param inputStream
     * @param key
     * @param type
     * @param metedata
     */
    public void ueditorUpload(InputStream inputStream, String key, String type, Object metedata) {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(ueditor_bucket);
        try {
            Response response = uploadManager.put(inputStream, key, upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            log.info(putRet.key);
            log.info(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error(r.toString());
            try {
                log.error(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }


    /**
     *
     根据名字删除文件
     */
    public void ueditorDeleteFile(String filename) {
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(ueditor_bucket,filename);
        } catch (QiniuException e) {
            //删除失败
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
