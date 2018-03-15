package xyz.sanshan.search.convert;

import org.modelmapper.ModelMapper;
import xyz.sanshan.search.pojo.DO.ElasticUeditorBlogDO;
import xyz.sanshan.search.pojo.DTO.UeditorBlogDTO;

public class UserConvert {

    private static final ModelMapper MODEL_MAPPER =new ModelMapper();


    public static ElasticUeditorBlogDO dtoToElastic(UeditorBlogDTO ueditorBlogDTO) {
        return  MODEL_MAPPER.map(ueditorBlogDTO,ElasticUeditorBlogDO.class);
    }

}
