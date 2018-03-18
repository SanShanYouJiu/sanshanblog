package xyz.sanshan.main.service.convent;

import com.github.pagehelper.PageInfo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import xyz.sanshan.main.pojo.dto.UeditorBlogDTO;
import xyz.sanshan.main.pojo.entity.UeditorBlogDO;

import java.util.List;
import java.util.Objects;

public class UeditorEditorConvert {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public static UeditorBlogDTO doToDto(UeditorBlogDO uEditorBlogDO) {
        if (Objects.isNull(uEditorBlogDO)) {
            return null;
        }
        return MODEL_MAPPER.map(uEditorBlogDO, UeditorBlogDTO.class);
    }

    public static List<UeditorBlogDTO> doToDtoList(List<UeditorBlogDO> uEditorBlogDOS) {
        return MODEL_MAPPER.map(uEditorBlogDOS,new TypeToken<List<UeditorBlogDTO>>(){}.getType());
    }




    public static PageInfo<UeditorBlogDTO> doToDtoPage(PageInfo<UeditorBlogDO> uEditorBlogDTOPageInfo) {
        List<UeditorBlogDO> list = uEditorBlogDTOPageInfo.getList();
        List<UeditorBlogDTO> uEditorBlogDTOS = UeditorEditorConvert.doToDtoList(list);
        return new PageInfo<UeditorBlogDTO>(uEditorBlogDTOS);
    }


}
