package xyz.sanshan.service.convent;

import xyz.sanshan.pojo.dto.BlogVoteDTO;
import xyz.sanshan.pojo.entity.BlogVoteDO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;
import java.util.Objects;

/**
 */
public class BlogVoteConvert {
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public static BlogVoteDTO doToDto(BlogVoteDO blogVoteDO) {
        if (Objects.isNull(blogVoteDO)) {
            return null;
        }
        return MODEL_MAPPER.map(blogVoteDO, BlogVoteDTO.class);
    }


    public static List<BlogVoteDTO> doToDtoList(List<BlogVoteDO> blogVoteDOS) {
        return MODEL_MAPPER.map(blogVoteDOS,new TypeToken<List<BlogVoteDTO>>(){}.getType());
    }
}
