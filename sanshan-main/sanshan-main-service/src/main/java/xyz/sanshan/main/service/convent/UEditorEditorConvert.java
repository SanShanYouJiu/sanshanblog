package xyz.sanshan.main.service.convent;

import com.github.pagehelper.PageInfo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import xyz.sanshan.main.pojo.dto.UEditorBlogDTO;
import xyz.sanshan.main.pojo.entity.UEditorBlogDO;

import java.util.List;
import java.util.Objects;

public class UEditorEditorConvert {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public static UEditorBlogDTO doToDto(UEditorBlogDO uEditorBlogDO) {
        if (Objects.isNull(uEditorBlogDO)) {
            return null;
        }
        return MODEL_MAPPER.map(uEditorBlogDO, UEditorBlogDTO.class);
    }

    public static List<UEditorBlogDTO> doToDtoList(List<UEditorBlogDO> uEditorBlogDOS) {
        return MODEL_MAPPER.map(uEditorBlogDOS,new TypeToken<List<UEditorBlogDTO>>(){}.getType());
    }




    public static PageInfo<UEditorBlogDTO> doToDtoPage(PageInfo<UEditorBlogDO> uEditorBlogDTOPageInfo) {
        List<UEditorBlogDO> list = uEditorBlogDTOPageInfo.getList();
        List<UEditorBlogDTO> uEditorBlogDTOS = UEditorEditorConvert.doToDtoList(list);
        return new PageInfo<UEditorBlogDTO>(uEditorBlogDTOS);
    }


}
