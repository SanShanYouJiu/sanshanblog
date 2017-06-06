package com.sanshan.service.convent;


import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.dto.MarkDownBlogDTO;
import com.sanshan.pojo.entity.MarkDownBlogDO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;
import java.util.Objects;

public class MarkDownEditorConvert {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static MarkDownBlogDTO doToDto(MarkDownBlogDO markDownBlogDO) {
        if (Objects.isNull(markDownBlogDO)) {
            return null;
        }
        return modelMapper.map(markDownBlogDO, MarkDownBlogDTO.class);
     }


    public static List<MarkDownBlogDTO> doToDtoList(List<MarkDownBlogDO> markDownBlogDOS) {
        return modelMapper.map(markDownBlogDOS,new TypeToken<List<MarkDownBlogDTO>>(){}.getType());
    }


    public static PageInfo<MarkDownBlogDTO> doToDtoPage(PageInfo<MarkDownBlogDO> markDownBlogDOPageInfo) {
        List<MarkDownBlogDO> list = markDownBlogDOPageInfo.getList();
        List<MarkDownBlogDTO> markDownBlogDTOS = MarkDownEditorConvert.doToDtoList(list);
        return new PageInfo<MarkDownBlogDTO>(markDownBlogDTOS);
    }




}
