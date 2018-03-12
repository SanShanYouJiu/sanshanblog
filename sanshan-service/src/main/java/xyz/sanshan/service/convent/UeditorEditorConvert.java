package xyz.sanshan.service.convent;

import com.github.pagehelper.PageInfo;
import xyz.sanshan.pojo.dto.UeditorBlogDTO;
import xyz.sanshan.pojo.elastic.ElasticUeditorBlogDO;
import xyz.sanshan.pojo.entity.UeditorBlogDO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

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

    public static ElasticUeditorBlogDO dtoToElastic(UeditorBlogDTO ueditorBlogDTO) {
        return  MODEL_MAPPER.map(ueditorBlogDTO,ElasticUeditorBlogDO.class);
    }


    public static PageInfo<UeditorBlogDTO> doToDtoPage(PageInfo<UeditorBlogDO> uEditorBlogDTOPageInfo) {
        List<UeditorBlogDO> list = uEditorBlogDTOPageInfo.getList();
        List<UeditorBlogDTO> uEditorBlogDTOS = UeditorEditorConvert.doToDtoList(list);
        return new PageInfo<UeditorBlogDTO>(uEditorBlogDTOS);
    }


}
