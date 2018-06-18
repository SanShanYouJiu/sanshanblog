package xyz.sanshan.main.dao.mybatis;

import xyz.sanshan.main.pojo.entity.UEditorFileQuoteDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;

/**
 */
public interface UEditorFileQuoteMapper extends Mapper<UEditorFileQuoteDO> {

    @Update("update ueditor_file_quote set quote =quote+1,updated = #{updated}  where filename=#{filename}")
    int incrFilenameQuote(@Param("filename") String filename, @Param("updated")Date updated);

    @Update("update ueditor_file_quote set quote =quote-1,updated = #{updated} where filename=#{filename}")
    int decrFilenameQuote(@Param("filename") String filename,@Param("updated")Date updated);


    @Delete("DELETE FROM ueditor_file_quote where filename=#{filename}")
    int deleteByFilename(@Param("filename") String filename);
}
