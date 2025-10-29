package com.burgerbuilder.controller;

import com.burgerbuilder.dto.CreateOrderRequest;
import com.burgerbuilder.dto.OrderDto;
import com.burgerbuilder.entity.Order;
import com.burgerbuilder.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        log.debug("POST /api/orders - Creating order from cart for session: {}", request.getSessionId());
        OrderDto order = orderService.createOrderFromCart(request);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        log.debug("GET /api/orders/{} - Fetching order by id", orderId);
        OrderDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/order-number/{orderNumber}")
    public ResponseEntity<OrderDto> getOrderByOrderNumber(@PathVariable String orderNumber) {
        log.debug("GET /api/orders/order-number/{} - Fetching order by order number", orderNumber);
        OrderDto order = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/customer/{email}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerEmail(@PathVariable String email) {
        log.debug("GET /api/orders/customer/{} - Fetching orders by customer email", email);
        List<OrderDto> orders = orderService.getOrdersByCustomerEmail(email);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<OrderDto>> getOrdersBySessionId(@PathVariable String sessionId) {
        log.debug("GET /api/orders/session/{} - Fetching orders by session ID", sessionId);
        List<OrderDto> orders = orderService.getOrdersBySessionId(sessionId);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/history")
    public ResponseEntity<List<OrderDto>> getOrderHistory(@RequestParam(required = false) String email) {
        log.debug("GET /api/orders/history - Fetching order history for email: {}", email);
        List<OrderDto> orders = orderService.getOrderHistory(email);
        return ResponseEntity.ok(orders);
    }
    
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Order.OrderStatus status) {
        log.debug("PUT /api/orders/{}/status - Updating order status to: {}", orderId, status);
        OrderDto order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(@PathVariable Order.OrderStatus status) {
        log.debug("GET /api/orders/status/{} - Fetching orders by status", status);
        List<OrderDto> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }
    
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }
}
