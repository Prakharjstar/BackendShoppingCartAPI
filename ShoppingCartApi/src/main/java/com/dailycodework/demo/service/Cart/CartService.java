package com.dailycodework.demo.service.Cart;

import com.dailycodework.demo.model.Cart;
import com.dailycodework.demo.model.User;

import java.math.BigDecimal;

public interface CartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);


    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
