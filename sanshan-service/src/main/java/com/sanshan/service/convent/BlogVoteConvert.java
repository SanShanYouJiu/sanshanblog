package com.sanshan.service.convent;

import com.sanshan.pojo.dto.BlogVoteDTO;
import com.sanshan.pojo.entity.BlogVoteDO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;
import java.util.Objects;

/**
 */
public class BlogVoteConvert {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static BlogVoteDTO doToDto(BlogVoteDO blogVoteDO) {
        if (Objects.isNull(blogVoteDO)) {
            return null;
        }
        return modelMapper.map(blogVoteDO, BlogVoteDTO.class);
    }


    public static List<BlogVoteDTO> doToDtoList(List<BlogVoteDO> blogVoteDOS) {
        return modelMapper.map(blogVoteDOS,new TypeToken<List<BlogVoteDTO>>(){}.getType());
    }
}
