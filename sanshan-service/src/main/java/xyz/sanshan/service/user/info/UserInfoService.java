package xyz.sanshan.service.user.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sanshan.dao.mongo.UserRepository;
import xyz.sanshan.pojo.dto.UserDTO;
import xyz.sanshan.pojo.entity.UserDO;
import xyz.sanshan.service.convent.UserConvert;
import xyz.sanshan.service.user.cache.UserBlogCacheService;
import xyz.sanshan.service.vo.BlogVO;
import xyz.sanshan.service.vo.ResponseMsgVO;

import java.util.List;

/**
 */
@Service
public class UserInfoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBlogCacheService userBlogCacheService;

    public void  getUserInfo(String username, ResponseMsgVO responseMsgVO){
        UserDO userDO = userRepository.findByUsername(username);
        UserDTO userDTO = UserConvert.doToDto(userDO);
        responseMsgVO.buildOKWithData(userDTO);
    }

    public List<BlogVO> getUserBlogs(String username) {
         return userBlogCacheService.getUserBlogs(username);
    }
}
