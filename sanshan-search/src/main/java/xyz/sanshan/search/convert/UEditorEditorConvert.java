package xyz.sanshan.search.convert;

import org.modelmapper.ModelMapper;
import xyz.sanshan.search.pojo.DO.ElasticUEditorBlogDO;
import xyz.sanshan.search.pojo.DTO.UEditorBlogDTO;

import java.util.Objects;

public class UEditorEditorConvert {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public static ElasticUEditorBlogDO dtoToElastic(UEditorBlogDTO UEditorBlogDTO) {
        if (Objects.isNull(UEditorBlogDTO)) {
            return null;
        }
        return  MODEL_MAPPER.map(UEditorBlogDTO,ElasticUEditorBlogDO.class);
    }


}
