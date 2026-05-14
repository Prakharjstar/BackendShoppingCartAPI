package com.dailycodework.demo.Repositories;

import com.dailycodework.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order , Long> {
}
