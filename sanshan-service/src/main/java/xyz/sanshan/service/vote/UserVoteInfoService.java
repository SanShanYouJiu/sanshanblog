package xyz.sanshan.service.vote;

import xyz.sanshan.dao.mongo.UserRepository;
import xyz.sanshan.pojo.dto.BlogVoteDTO;
import xyz.sanshan.pojo.entity.UserDO;
import xyz.sanshan.service.user.info.UserInfoService;
import xyz.sanshan.service.vo.BlogVO;
import xyz.sanshan.service.vo.ResponseMsgVO;
import xyz.sanshan.service.vo.UserVoteInfoVO;
import xyz.sanshan.common.info.PosCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UserVoteInfoService {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogVoteInfoService blogVoteInfoService;

    public void getInfo(String username,ResponseMsgVO responseMsgVO) {
        UserDO userDO = userRepository.findByUsername(username);
        if (Objects.isNull(userDO)) {
            responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.CODE_ERROR, "没有这个用户");
            return;
        }
        List<BlogVO> userBlogs = userInfoService.getUserBlogs(username);
        int amountFavours = 0 ;
        int amountTreads = 0;
        Map<Long, Integer> blogFavourMap = new HashMap<>();
        Map<Long, Integer> blogTreadMap = new HashMap<>();
        for (int i = 0; i < userBlogs.size(); i++) {
            BlogVO blog = userBlogs.get(i);
            Long blogId = blog.getId();
            ResponseMsgVO blogVoteDTOWrap = new ResponseMsgVO();
            blogVoteInfoService.queryBlogInfo(blogId, blogVoteDTOWrap);
            if (blogVoteDTOWrap.getStatus().equals(0)) {
                BlogVoteDTO blogVote = (BlogVoteDTO) blogVoteDTOWrap.getData();
                amountFavours+=blogVote.getFavours();
                amountTreads+= blogVote.getTreads();
                blogFavourMap.put(blogId, blogVote.getFavours());
                blogTreadMap.put(blogId, blogVote.getTreads());
            }else {
                //responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, "出现未知错误");
            }
        }
        UserVoteInfoVO userVoteInfoVO = new UserVoteInfoVO(amountFavours,amountTreads,blogFavourMap,blogTreadMap);
        responseMsgVO.buildOKWithData(userVoteInfoVO);
    }
}
