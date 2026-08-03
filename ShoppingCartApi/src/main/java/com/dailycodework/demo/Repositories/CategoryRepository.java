package com.dailycodework.demo.Repositories;

import com.dailycodework.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category , Long> {

    Category findByName(String name);

    boolean existsByName(String name);
}
