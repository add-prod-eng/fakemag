package main.java.ro.unibuc.hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.hello.model.ProductEntity;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByCategoryId(Long categoryId);
}
