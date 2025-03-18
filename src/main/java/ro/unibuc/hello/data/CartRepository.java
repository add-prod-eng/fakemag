package ro.unibuc.hello.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CartRepository extends MongoRepository<CartEntity, String> {

    CartEntity findByUserId(String userId);
    CartEntity findByProductId(String productId);
    CartEntity findByDate(Date date);
    
}
