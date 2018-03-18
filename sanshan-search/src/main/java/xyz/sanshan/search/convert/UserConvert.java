package xyz.sanshan.search.convert;

import org.modelmapper.ModelMapper;
import xyz.sanshan.search.pojo.DO.ElasticUserDO;
import xyz.sanshan.search.pojo.DTO.UserDTO;

import java.util.Objects;

public class UserConvert {

    private static final ModelMapper MODEL_MAPPER =new ModelMapper();


    public static ElasticUserDO dtoToElasticDO(UserDTO userDTO) {
        if (Objects.isNull(userDTO)) {
            return null;
        }
        return MODEL_MAPPER.map(userDTO,ElasticUserDO.class);
    }


}
