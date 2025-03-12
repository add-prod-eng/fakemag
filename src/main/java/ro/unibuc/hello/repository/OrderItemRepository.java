package main.java.ro.unibuc.hello.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import main.java.ro.unibuc.hello.data.OrderItem;
 
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}