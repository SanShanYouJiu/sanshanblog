package xyz.sanshan.main.service.recommend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sanshan.main.dao.mongo.UserRepository;
import xyz.sanshan.main.dao.mongo.recommend.BlogRecommendRepository;
import xyz.sanshan.main.dao.mongo.recommend.UserRecommendRepository;
import xyz.sanshan.main.pojo.entity.recommend.UserRecommendDO;
import xyz.sanshan.main.service.user.cache.UserBlogCacheService;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class UserRecommendService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRecommendRepository userRecommendRepository;

    @Autowired
    private UserBlogCacheService userBlogCacheService;

    @Autowired
    private BlogRecommendRepository blogRecommendRepository;

    private final Integer generateUsers = 5;


    /**
     * 完成用户推荐
     * @return
     */
    public List<UserRecommendDO>  generateUsers(){
        List<UserRecommendDO> recommendUsers = new LinkedList<>();
        log.info("生成一次 推荐用户数据");

        //List<UserDO> users = userRepository.findAll();
        //for (int i = 0; i < users.size(); i++) {
        //    UserDO userDO = users.get(i);
        //    String username = userDO.getUsername();
        //    List<BlogVO> blogVOS = userBlogCacheService.getUserBlogs(username);
        //    for (int j = 0; j <blogVOS.size() ; j++) {
        //        Long id = blogVOS.get(i).getId();
        //        BlogRecommendDO blogRecommendDO = blogRecommendRepository.findOne(id);
        //        Double blogRate = blogRecommendDO.getRecommendRate();
        //    }
        //}
        return recommendUsers;
    }


}
