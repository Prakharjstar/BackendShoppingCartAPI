package com.dailycodework.demo.service.Order;

import com.dailycodework.demo.model.Order;

import java.util.List;

public interface OrderService {

    Order placeOrder(Long userId);
    Order getOrder(Long orderId);

    List<Order> getUserOrders(Long userId);
}
