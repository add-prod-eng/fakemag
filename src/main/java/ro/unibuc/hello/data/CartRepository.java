package ro.unibuc.hello.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CartRepository extends MongoRepository<CartEntity, String> {

    List<CartEntity> findByUserId(String userId);
    List<CartEntity> findByProductId(String productId);
    List<CartEntity> findByDate(Date date);
    
}
