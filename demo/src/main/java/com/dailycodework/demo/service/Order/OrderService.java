package com.dailycodework.demo.service.Order;

import com.dailycodework.demo.model.Order;

public interface OrderService {

    Order placeOrder(Long userId);
    Order getOrder(Long orderId);

}
