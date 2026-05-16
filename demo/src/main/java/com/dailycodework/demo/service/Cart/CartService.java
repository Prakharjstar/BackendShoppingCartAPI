package com.dailycodework.demo.service.Cart;

import com.dailycodework.demo.model.Cart;

import java.math.BigDecimal;

public interface CartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Long initializeNewCart();

    Cart getCartByUserId(Long userId);
}
