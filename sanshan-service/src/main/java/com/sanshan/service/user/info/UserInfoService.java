package com.sanshan.service.user.info;

import com.sanshan.dao.mongo.UserRepository;
import com.sanshan.pojo.dto.UserDTO;
import com.sanshan.pojo.entity.UserDO;
import com.sanshan.service.convent.UserConvert;
import com.sanshan.service.user.cache.UserBlogCacheService;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.service.vo.ResponseMsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
