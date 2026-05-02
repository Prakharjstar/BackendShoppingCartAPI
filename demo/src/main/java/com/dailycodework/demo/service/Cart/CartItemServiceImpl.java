package com.dailycodework.demo.service.Cart;

import com.dailycodework.demo.Exceptions.ResourceNotFoundException;
import com.dailycodework.demo.Repositories.CartItemRepository;
import com.dailycodework.demo.Repositories.CartRepository;
import com.dailycodework.demo.model.Cart;
import com.dailycodework.demo.model.CartItem;
import com.dailycodework.demo.model.Product;
import com.dailycodework.demo.service.product.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final CartService cartService;
    private final CartRepository cartRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, ProductService productService, CartService cartService, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
    }

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {

        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getCartItems()
        .stream().filter(item ->item.getProduct().getId().equals(productId)).findFirst().orElse(new CartItem());

        if(cartItem.getId()==null){
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }
        else{
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);



    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId , productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);


    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getCartItems()
                .stream()
                .filter(item-> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item->{
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);

    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId){
        Cart cart = cartService.getCart(cartId);
        return cart.getCartItems().stream().filter(item->item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(()-> new ResourceNotFoundException("Product not found") );
    }
}
