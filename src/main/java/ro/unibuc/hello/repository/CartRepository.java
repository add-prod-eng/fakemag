package main.java.ro.unibuc.hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.hello.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserId(Long userId);
}
