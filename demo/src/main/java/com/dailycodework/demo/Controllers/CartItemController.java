package com.dailycodework.demo.Controllers;

import com.dailycodework.demo.Exceptions.ResourceNotFoundException;
import com.dailycodework.demo.Response.ApiResponse;
import com.dailycodework.demo.model.Cart;
import com.dailycodework.demo.model.User;
import com.dailycodework.demo.service.Cart.CartItemService;
import com.dailycodework.demo.service.Cart.CartService;
import com.dailycodework.demo.service.User.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    private final CartItemService cartItemService;
    private final CartService cartService;
    private final UserService userService;

    public CartItemController(CartItemService cartItemService, CartService cartService, UserService userService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart( @RequestParam Long productId, @RequestParam Integer quantity) {
        try {
            User user = userService.getUserById(1L);

             Cart cart=  cartService.initializeNewCart(user);



            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Add Item Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId){
        try {
            cartItemService.removeItemFromCart(cartId , itemId);
            return ResponseEntity.ok(new ApiResponse("Remove Item Success" , null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @PutMapping("/cart/{cartId}/item/{itemId}/updates")
    public ResponseEntity<ApiResponse> updateItemQuantity( @PathVariable Long cartId , @PathVariable Long itemId ,@RequestParam  Integer quantity){
        try {
            cartItemService.updateItemQuantity(cartId , itemId , quantity);
            return ResponseEntity.ok(new ApiResponse("Update Item Success" , null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() , null));
        }

    }
}
