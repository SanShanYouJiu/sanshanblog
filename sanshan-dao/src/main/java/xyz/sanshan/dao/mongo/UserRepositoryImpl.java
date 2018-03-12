package xyz.sanshan.dao.mongo;

import com.mongodb.WriteResult;
import xyz.sanshan.pojo.entity.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;


import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;


public class UserRepositoryImpl implements CustomUserRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public WriteResult changePassword( final String username, final String password) {
        Query query = new Query();
        query.addCriteria(new Criteria("username").is(username));
        Update update = new Update();
        update.set("password", password);
        update.set("lastPasswordResetDate", new Date());
        return this.mongoTemplate.updateFirst(query, update, UserDO.class);
    }

    @Override
    public WriteResult changeUserInfo(UserDO userDO) {
        Query query = new Query();
        query.addCriteria(new Criteria("username").is(userDO.getUsername()));
        Update update = new Update();
        if (userDO.getBlogLink()!=null){
            update.set("blogLink", userDO.getBlogLink());
        }
        if (userDO.getAvatar()!=null){
            update.set("avatar", userDO.getAvatar());
        }
        return this.mongoTemplate.upsert(query, update, UserDO.class);
    }


}
