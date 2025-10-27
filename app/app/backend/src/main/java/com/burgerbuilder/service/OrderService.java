package com.burgerbuilder.service;

import com.burgerbuilder.dto.CreateOrderRequest;
import com.burgerbuilder.dto.OrderDto;
import com.burgerbuilder.dto.OrderItemDto;
import com.burgerbuilder.entity.*;
import com.burgerbuilder.exception.OrderException;
import com.burgerbuilder.exception.ResourceNotFoundException;
import com.burgerbuilder.repository.CartItemRepository;
import com.burgerbuilder.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final IngredientService ingredientService;
    
    public OrderDto createOrderFromCart(CreateOrderRequest request) {
        log.debug("Creating order from cart for session: {}", request.getSessionId());
        
        // Validate cart items exist and belong to the session
        List<CartItem> cartItems = validateCartItems(request.getSessionId(), request.getCartItemIds());
        
        if (cartItems.isEmpty()) {
            throw new OrderException("Cart is empty or cart items not found");
        }
        
        // Calculate total amount
        BigDecimal totalAmount = calculateOrderTotal(cartItems);
        
        // Create order
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setTotalAmount(totalAmount);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        // Generate order number
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        
        // Create order items from cart items
        List<OrderItem> orderItems = createOrderItems(order, cartItems);
        order.setOrderItems(orderItems);
        
        // Save order
        order = orderRepository.save(order);
        
        // Remove cart items after successful order creation
        cartItemRepository.deleteBySessionId(request.getSessionId());
        
        log.info("Created order successfully: orderId={}, orderNumber={}", order.getId(), order.getOrderNumber());
        return convertToDto(order);
    }
    
    public OrderDto getOrderById(Long orderId) {
        log.debug("Fetching order with id: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        return convertToDto(order);
    }
    
    public OrderDto getOrderByOrderNumber(String orderNumber) {
        log.debug("Fetching order with order number: {}", orderNumber);
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));
        
        return convertToDto(order);
    }
    
    public OrderDto updateOrderStatus(Long orderId, Order.OrderStatus status) {
        log.debug("Updating order status for orderId: {} to: {}", orderId, status);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        
        order = orderRepository.save(order);
        
        log.info("Updated order status successfully: orderId={}, newStatus={}", orderId, status);
        return convertToDto(order);
    }
    
    public List<OrderDto> getOrdersByCustomerEmail(String email) {
        log.debug("Fetching orders for customer email: {}", email);
        List<Order> orders = orderRepository.findByCustomerEmailOrderByCreatedAtDesc(email);
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<OrderDto> getOrdersBySessionId(String sessionId) {
        log.debug("Fetching orders for session ID: {}", sessionId);
        // Note: We don't store sessionId in orders directly, so we'll need to implement this differently
        // For now, return recent orders or implement a different approach
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<OrderDto> getOrderHistory(String email) {
        log.debug("Fetching order history for email: {}", email);
        if (email != null && !email.trim().isEmpty()) {
            return getOrdersByCustomerEmail(email);
        } else {
            // Return all orders for admin view
            List<Order> orders = orderRepository.findAll();
            return orders.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
    }
    
    public List<OrderDto> getOrdersByStatus(Order.OrderStatus status) {
        log.debug("Fetching orders with status: {}", status);
        List<Order> orders = orderRepository.findByStatusOrderByCreatedAtDesc(status);
        
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private List<CartItem> validateCartItems(String sessionId, List<Long> cartItemIds) {
        List<CartItem> validCartItems = new ArrayList<>();
        
        for (Long cartItemId : cartItemIds) {
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));
            
            if (!cartItem.getSessionId().equals(sessionId)) {
                throw new OrderException("Cart item does not belong to this session");
            }
            
            validCartItems.add(cartItem);
        }
        
        return validCartItems;
    }
    
    private BigDecimal calculateOrderTotal(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private List<OrderItem> createOrderItems(Order order, List<CartItem> cartItems) {
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setIngredient(cartItem.getIngredient());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            
            // Create order layers from burger layers
            List<OrderLayer> orderLayers = createOrderLayers(orderItem, cartItem.getBurgerLayers());
            orderItem.setOrderLayers(orderLayers);
            
            orderItems.add(orderItem);
        }
        
        return orderItems;
    }
    
    private List<OrderLayer> createOrderLayers(OrderItem orderItem, List<BurgerLayer> burgerLayers) {
        List<OrderLayer> orderLayers = new ArrayList<>();
        
        if (burgerLayers != null) {
            for (BurgerLayer burgerLayer : burgerLayers) {
                OrderLayer orderLayer = new OrderLayer();
                orderLayer.setOrderItem(orderItem);
                orderLayer.setIngredient(burgerLayer.getIngredient());
                orderLayer.setLayerOrder(burgerLayer.getLayerOrder());
                orderLayer.setQuantity(burgerLayer.getQuantity());
                orderLayer.setUnitPrice(burgerLayer.getUnitPrice());
                
                orderLayers.add(orderLayer);
            }
        }
        
        return orderLayers;
    }
    
    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setCustomerName(order.getCustomerName());
        dto.setCustomerEmail(order.getCustomerEmail());
        dto.setCustomerPhone(order.getCustomerPhone());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        
        if (order.getOrderItems() != null) {
            dto.setOrderItems(order.getOrderItems().stream()
                    .map(this::convertOrderItemToDto)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    private OrderItemDto convertOrderItemToDto(OrderItem orderItem) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(orderItem.getId());
        dto.setOrderId(orderItem.getOrder().getId());
        dto.setIngredientId(orderItem.getIngredient().getId());
        dto.setIngredient(ingredientService.convertToDto(orderItem.getIngredient()));
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitPrice(orderItem.getUnitPrice());
        dto.setTotalPrice(orderItem.getTotalPrice());
        
        if (orderItem.getOrderLayers() != null) {
            dto.setOrderLayers(orderItem.getOrderLayers().stream()
                    .map(this::convertOrderLayerToDto)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    private com.burgerbuilder.dto.OrderLayerDto convertOrderLayerToDto(OrderLayer orderLayer) {
        com.burgerbuilder.dto.OrderLayerDto dto = new com.burgerbuilder.dto.OrderLayerDto();
        dto.setId(orderLayer.getId());
        dto.setOrderItemId(orderLayer.getOrderItem().getId());
        dto.setIngredientId(orderLayer.getIngredient().getId());
        dto.setIngredient(ingredientService.convertToDto(orderLayer.getIngredient()));
        dto.setLayerOrder(orderLayer.getLayerOrder());
        dto.setQuantity(orderLayer.getQuantity());
        dto.setUnitPrice(orderLayer.getUnitPrice());
        return dto;
    }
}
