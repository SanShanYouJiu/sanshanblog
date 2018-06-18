package xyz.sanshan.main.service.convent;

import org.modelmapper.ModelMapper;
import xyz.sanshan.main.pojo.dto.CommonBlogDTO;
import xyz.sanshan.main.pojo.entity.recommend.BlogRecommendDO;
import xyz.sanshan.main.service.vo.BlogVO;

public class BlogRecommendConvert {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public  static BlogRecommendDO dtoToDo(CommonBlogDTO dto) {
        return MODEL_MAPPER.map(dto, BlogRecommendDO.class);
    }

    public static BlogRecommendDO voToDto(BlogVO vo) {
        return MODEL_MAPPER.map(vo, BlogRecommendDO.class);
    }


}
