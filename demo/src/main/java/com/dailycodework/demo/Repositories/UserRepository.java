package com.dailycodework.demo.Repositories;

import com.dailycodework.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String  email);
}
