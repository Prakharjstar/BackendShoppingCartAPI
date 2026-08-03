package com.dailycodework.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Cart() {
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void addItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
        updateTotalAmount();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void removeItem(CartItem cartItem) {
        this.cartItems.remove(cartItem);
        cartItem.setCart(null);
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        this.totalAmount = this.cartItems.stream()
                .map(item -> item.getUnitPrice() == null
                        ? BigDecimal.ZERO
                        : item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}