package xyz.sanshan.main.service.convent;


import org.modelmapper.ModelMapper;
import xyz.sanshan.main.pojo.dto.recommend.RecommendDTO;
import xyz.sanshan.main.pojo.entity.recommend.RecommendDO;

import java.util.Objects;

public class RecommendConvert {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public static RecommendDTO doToDto(RecommendDO recommendDO){
        if (Objects.isNull(recommendDO)) {
            return null;
        }
        return MODEL_MAPPER.map(recommendDO,RecommendDTO.class);
    }

    public static RecommendDO dtoToDo(RecommendDTO recommendDTO){
        if (Objects.isNull(recommendDTO)) {
            return null;
        }
        return MODEL_MAPPER.map(recommendDTO,RecommendDO.class);
    }

}
