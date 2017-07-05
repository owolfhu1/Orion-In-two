package com.example.demo.Repositories;

import com.example.demo.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User findByEmail(String email);
}
