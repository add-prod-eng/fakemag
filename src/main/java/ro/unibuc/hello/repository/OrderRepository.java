package main.java.ro.unibuc.hello.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import main.java.ro.unibuc.hello.data.OrderEntity;
 
import java.util.List;
 
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserId(String userId);
}
