package xyz.sanshan.search.convert;


import org.modelmapper.ModelMapper;
import xyz.sanshan.search.pojo.DO.ElasticMarkDownBlogDO;
import xyz.sanshan.search.pojo.DTO.MarkDownBlogDTO;

import java.util.Objects;

public class MarkDownEditorConvert {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public static ElasticMarkDownBlogDO dtoToElastic(MarkDownBlogDTO markDownBlogDTO) {
        if (Objects.isNull(markDownBlogDTO)) {
            return null;
        }
        return  MODEL_MAPPER.map(markDownBlogDTO,ElasticMarkDownBlogDO.class);
    }


}
