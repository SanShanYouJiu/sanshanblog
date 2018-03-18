package xyz.sanshan.main.service.convent;

import com.github.pagehelper.PageInfo;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import xyz.sanshan.main.pojo.dto.UserDTO;
import xyz.sanshan.main.pojo.entity.UserDO;

import java.util.List;
import java.util.Objects;

public class UserConvert {

    private static final ModelMapper MODEL_MAPPER =new ModelMapper();

    static {
        PropertyMap<UserDO,UserDTO> typeMap = new PropertyMap<UserDO, UserDTO>() {
            @Override
            protected void configure() {
                map().setId(source.get_id());
            }
        };
        MODEL_MAPPER.addMappings(typeMap);
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



    public static PageInfo<UserDTO> doToDtoPage(PageInfo<UserDO> userDOPageInfo) {
        List<UserDO> list = userDOPageInfo.getList();
        List<UserDTO> userDTOS = UserConvert.doToDtoList(list);
        return new PageInfo<UserDTO>(userDTOS);
    }
}
