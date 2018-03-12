package xyz.sanshan.dao.mongo;

import xyz.sanshan.pojo.entity.FeedbackDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 */
@Repository
public interface FeedBackRepository extends MongoRepository<FeedbackDO, String> {

    FeedbackDO findByEmail(final String email);
}
