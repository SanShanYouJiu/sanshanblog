package xyz.sanshan.service.convent;

import com.github.pagehelper.PageInfo;
import xyz.sanshan.pojo.dto.UserDTO;
import xyz.sanshan.pojo.elastic.ElasticUserDO;
import xyz.sanshan.pojo.entity.UserDO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;

import java.util.List;
import java.util.Objects;

public class UserConvert {

    private static final ModelMapper MODEL_MAPPER =new ModelMapper();

    static {
        PropertyMap<UserDO, ElasticUserDO> typeMap =  new PropertyMap<UserDO, ElasticUserDO>() {
            @Override
            protected void configure() {
            map().setId(source.get_id());
            }
        } ;
        PropertyMap<UserDO,UserDTO> typeMap2 = new PropertyMap<UserDO, UserDTO>() {
            @Override
            protected void configure() {
                map().setId(source.get_id());
            }
        };
        MODEL_MAPPER.addMappings(typeMap);
        MODEL_MAPPER.addMappings(typeMap2);
    }

    public static UserDTO doToDto(UserDO userDO) {
        if (Objects.isNull(userDO)){
            return null;
        }
        return MODEL_MAPPER.map(userDO,UserDTO.class);
    }

    public static List<UserDTO> doToDtoList(List<UserDO> userDOS) {
        return MODEL_MAPPER.map(userDOS,new TypeToken<List<UserDTO>>(){}.getType());
    }

    public static ElasticUserDO dtoToElasticDO(UserDTO userDTO) {
        if (Objects.isNull(userDTO)) {
            return null;
        }
        return MODEL_MAPPER.map(userDTO,ElasticUserDO.class);
    }


    public static PageInfo<UserDTO> doToDtoPage(PageInfo<UserDO> userDOPageInfo) {
        List<UserDO> list = userDOPageInfo.getList();
        List<UserDTO> userDTOS = UserConvert.doToDtoList(list);
        return new PageInfo<UserDTO>(userDTOS);
    }
}
