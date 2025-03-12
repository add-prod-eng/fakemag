package  main.java.ro.unibuc.hello.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.hello.model.CartProduct;
 
import java.util.List;
 
@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
 
    List<CartProduct> findByCartId(Long cartId);
}

