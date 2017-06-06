package com.sanshan.service.convent;

import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.dto.UEditorBlogDTO;
import com.sanshan.pojo.entity.UEditorBlogDO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;
import java.util.Objects;

public class UeditorEditorConvert {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static UEditorBlogDTO doToDto(UEditorBlogDO uEditorBlogDO) {
        if (Objects.isNull(uEditorBlogDO)) {
            return null;
        }
        return modelMapper.map(uEditorBlogDO, UEditorBlogDTO.class);
    }

    public static List<UEditorBlogDTO> doToDtoList(List<UEditorBlogDO> uEditorBlogDOS) {
        return modelMapper.map(uEditorBlogDOS,new TypeToken<List<UEditorBlogDTO>>(){}.getType());
    }


    public static PageInfo<UEditorBlogDTO> doToDtoPage(PageInfo<UEditorBlogDO> uEditorBlogDTOPageInfo) {
        List<UEditorBlogDO> list = uEditorBlogDTOPageInfo.getList();
        List<UEditorBlogDTO> uEditorBlogDTOS = UeditorEditorConvert.doToDtoList(list);
        return new PageInfo<UEditorBlogDTO>(uEditorBlogDTOS);
    }


}
