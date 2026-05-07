package com.dailycodework.demo.service.Cart;

import com.dailycodework.demo.Exceptions.ResourceNotFoundException;
import com.dailycodework.demo.Repositories.CartItemRepository;
import com.dailycodework.demo.Repositories.CartRepository;
import com.dailycodework.demo.model.Cart;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;



    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Cart getCart(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);

    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }


    @Override
    public Long initializeNewCart() {
        Cart newCart = new Cart();
        return cartRepository.save(newCart).getId();
    }
}
