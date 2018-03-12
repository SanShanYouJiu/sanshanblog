package xyz.sanshan.service.recommend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sanshan.dao.mongo.UserRepository;
import xyz.sanshan.pojo.dto.UserDTO;
import xyz.sanshan.service.user.info.UserInfoService;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class UserRecommendService {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserRepository userRepository;

    private final Integer generateUsers = 3;


    /**
     * TODO 完成用户推荐
     * @return
     */
    public List<UserDTO>  generateUsers(){
        List<UserDTO> recommendUsers = new LinkedList<>();
        log.info("生成一次 用户推荐数据");

        return recommendUsers;
    }
}