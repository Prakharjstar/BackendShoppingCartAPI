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
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            CartService cartService,
                            ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public Order placeOrder(Long userId) {

        Cart cart = cartService.getCartByUserId(userId);

        Order order = createOrder(cart);

        List<OrderItem> orderItems = createOrderItems(cart);

        orderItems.forEach(item -> {
            item.setOrder(order);
            order.getOrderItems().add(item);
        });

        order.setTotalAmount(calculateTotalAmount(orderItems));

        Order savedOrder = orderRepository.save(order);


        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Cart cart) {

        return cart.getCartItems().stream().map(cartItem -> {

            Product product = cartItem.getProduct();

            product.setInventory(
                    product.getInventory() - cartItem.getQuantity()
            );

            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getUnitPrice());

            return orderItem;

        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {

        return orderItems.stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {

        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {

        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public OrderDto convertToDto(Order order) {
        System.out.println("Order ID = " + order.getOrderId());
        System.out.println("Items Count = " + order.getOrderItems().size());
        return modelMapper.map(order, OrderDto.class);
    }
}