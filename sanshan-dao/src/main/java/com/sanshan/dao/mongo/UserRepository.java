package com.sanshan.dao.mongo;

import com.sanshan.pojo.entity.UserDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserDO, String>,CustomUserRepository{

    UserDO findByUsername(final String usernmae);

    UserDO findByEmail(final String email);

}
