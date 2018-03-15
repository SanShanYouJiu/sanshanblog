package xyz.sanshan.search.convert;


import org.modelmapper.ModelMapper;
import xyz.sanshan.search.pojo.DO.ElasticMarkDownBlogDO;
import xyz.sanshan.search.pojo.DTO.MarkDownBlogDTO;

public class MarkDownEditorConvert {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public static ElasticMarkDownBlogDO dtoToElastic(MarkDownBlogDTO markDownBlogDTO) {
        return  MODEL_MAPPER.map(markDownBlogDTO,ElasticMarkDownBlogDO.class);
    }


}
