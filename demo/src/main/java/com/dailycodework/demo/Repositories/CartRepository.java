package com.dailycodework.demo.Repositories;

import com.dailycodework.demo.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
