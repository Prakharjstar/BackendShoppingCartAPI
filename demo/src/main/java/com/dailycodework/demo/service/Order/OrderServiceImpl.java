package com.dailycodework.demo.service.Order;

import com.dailycodework.demo.Exceptions.ResourceNotFoundException;

import com.dailycodework.demo.Repositories.OrderRepository;
import com.dailycodework.demo.Repositories.ProductRepository;
import com.dailycodework.demo.dto.OrderDto;
import com.dailycodework.demo.enums.OrderStatus;
import com.dailycodework.demo.model.Cart;
import com.dailycodework.demo.model.Order;
import com.dailycodework.demo.model.OrderItem;
import com.dailycodework.demo.model.Product;
import com.dailycodework.demo.service.Cart.CartService;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service

public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final  ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, CartService cartService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }




    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order,cart);
        order.setOrderItems( new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }


    private List<OrderItem> createOrderItems(Order order , Cart cart){
        return cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory()-cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice());
        }).toList();
    }


    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
        return orderItemList.stream().map(item ->item.getPrice()
                .multiply(new BigDecimal(item.getQuantity()))).reduce(BigDecimal.ZERO ,BigDecimal::add);
    }


    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId).
                map(this :: convertToDto).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    }

    @Override
    public List<OrderDto> getUserOrders(Long userId){
        List<Order> order = orderRepository.findByUserId(userId);
        return order.stream().map(this :: convertToDto).toList();
    }

    private OrderDto convertToDto(Order order){
        return modelMapper.map(order , OrderDto.class);
    }


}
