package ro.unibuc.hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.hello.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
