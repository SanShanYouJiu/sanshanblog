package xyz.sanshan.main.service.user.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sanshan.main.dao.mongo.UserRepository;
import xyz.sanshan.main.pojo.dto.UserDTO;
import xyz.sanshan.main.pojo.entity.UserDO;
import xyz.sanshan.main.service.convent.UserConvert;
import xyz.sanshan.main.service.user.cache.UserBlogCacheService;
import xyz.sanshan.main.service.vo.BlogVO;
import xyz.sanshan.common.vo.ResponseMsgVO;

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
