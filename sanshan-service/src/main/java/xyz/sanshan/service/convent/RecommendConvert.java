package xyz.sanshan.service.convent;


import xyz.sanshan.pojo.dto.RecommendDTO;
import xyz.sanshan.pojo.entity.RecommendDO;
import org.modelmapper.ModelMapper;

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
