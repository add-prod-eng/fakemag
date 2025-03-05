package main.java.ro.unibuc.hello.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    public List<UserEntity> findAll() {
        return null;
    }

    public UserEntity findById(String id) {
        return null;
    }

    public UserEntity save(UserEntity user) {
        return null;
    }

    public void deleteById(String id) {
    }

    public UserEntity findByUsername(String username) {
        return null;
    }
}
