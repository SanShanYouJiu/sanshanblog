package xyz.sanshan.dao.mongo;

import com.mongodb.gridfs.GridFSDBFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.List;

/**
 */
@Repository
public class FileOperation {

    @Autowired
    private GridFsTemplate gridFsTemplate;


    /**
     * 存入文件
     * @param content  文件
     * @param filename 文件名
     * @param type 类型
     * @param metedata 元数据 元数据其实就是数据的数据 其他附带的一些信息
     */
    public void saveFile(InputStream content, String filename, String type, Object metedata) {
        gridFsTemplate.store(content, filename, type, metedata);
    }

    /**
     *  获得文件
     * @param filename 文件名
     * @return 返回匹配图片集合
     */
    public List<GridFSDBFile> getFile(String filename) {
        return gridFsTemplate.find(new Query().addCriteria(Criteria.where("filename").is(filename)));
    }


    /**
     * 删除文件
     * @param filename 文件名
     */
    public void deleteFile(String filename){
        gridFsTemplate.delete(new Query().addCriteria(Criteria.where("filename").is(filename)));
    }
}
