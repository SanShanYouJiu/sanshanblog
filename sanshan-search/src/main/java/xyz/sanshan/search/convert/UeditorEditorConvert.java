package xyz.sanshan.search.convert;

import org.modelmapper.ModelMapper;
import xyz.sanshan.search.pojo.DO.ElasticUeditorBlogDO;
import xyz.sanshan.search.pojo.DTO.UeditorBlogDTO;

import java.util.Objects;

public class UeditorEditorConvert {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public static ElasticUeditorBlogDO dtoToElastic(UeditorBlogDTO ueditorBlogDTO) {
        if (Objects.isNull(ueditorBlogDTO)) {
            return null;
        }
        return  MODEL_MAPPER.map(ueditorBlogDTO,ElasticUeditorBlogDO.class);
    }


}
