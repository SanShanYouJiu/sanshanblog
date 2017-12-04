package com.sanshan.service.convent;

import com.sanshan.pojo.dto.IpBlogVoteDTO;
import com.sanshan.pojo.entity.IpBlogVoteDO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;
import java.util.Objects;

/**
 */
public class IpBlogVoteConvert {
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();


    public static IpBlogVoteDTO doToDto(IpBlogVoteDO ipBlogVoteDO) {
        if (Objects.isNull(ipBlogVoteDO)) {
            return null;
        }
        return MODEL_MAPPER.map(ipBlogVoteDO, IpBlogVoteDTO.class);
    }


    public static List<IpBlogVoteDTO> doToDtoList(List<IpBlogVoteDO> ipBlogVoteDOS) {
        return MODEL_MAPPER.map(ipBlogVoteDOS,new TypeToken<List<IpBlogVoteDTO>>(){}.getType());
    }

}
