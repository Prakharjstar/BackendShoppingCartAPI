package com.dailycodework.demo.service.Order;

import com.dailycodework.demo.dto.OrderDto;
import com.dailycodework.demo.model.Order;

import java.util.List;

public interface OrderService {

    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
