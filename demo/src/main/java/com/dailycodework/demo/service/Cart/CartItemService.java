package com.dailycodework.demo.service.Cart;

import com.dailycodework.demo.model.Cart;
import com.dailycodework.demo.model.CartItem;

public interface CartItemService {

    void  addItemToCart(Long cartId , Long productId , int quantity);
    void  removeItemFromCart(Long cartId , Long productId );
    void updateItemQuantity(Long cartId , Long productId , int quantity);

    CartItem getCartItem(Long cartId, Long productId);

    Cart getCartByUserId(Long userId);
}
