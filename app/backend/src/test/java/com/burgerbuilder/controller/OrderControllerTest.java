package com.burgerbuilder.controller;

import com.burgerbuilder.dto.CreateOrderRequest;
import com.burgerbuilder.dto.OrderDto;
import com.burgerbuilder.entity.Order;
import com.burgerbuilder.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createOrder_ShouldReturnOrder() throws Exception {
        // Given
        CreateOrderRequest request = new CreateOrderRequest();
        request.setSessionId("session123");
        request.setCustomerName("John Doe");
        request.setCustomerEmail("john@example.com");
        request.setCartItemIds(Arrays.asList(1L, 2L, 3L));

        OrderDto order = createOrderDto();
        when(orderService.createOrderFromCart(any(CreateOrderRequest.class))).thenReturn(order);

        // When & Then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderNumber").value("ORD-123456"))
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.customerEmail").value("john@example.com"))
                .andExpect(jsonPath("$.totalAmount").value(15.50))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(orderService).createOrderFromCart(any(CreateOrderRequest.class));
    }

    @Test
    void createOrder_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Given
        CreateOrderRequest request = new CreateOrderRequest();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOrderById_ShouldReturnOrder() throws Exception {
        // Given
        Long orderId = 1L;
        OrderDto order = createOrderDto();
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // When & Then
        mockMvc.perform(get("/api/orders/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderNumber").value("ORD-123456"))
                .andExpect(jsonPath("$.customerName").value("John Doe"));

        verify(orderService).getOrderById(orderId);
    }

    @Test
    void getOrderByOrderNumber_ShouldReturnOrder() throws Exception {
        // Given
        String orderNumber = "ORD-123456";
        OrderDto order = createOrderDto();
        when(orderService.getOrderByOrderNumber(orderNumber)).thenReturn(order);

        // When & Then
        mockMvc.perform(get("/api/orders/order-number/{orderNumber}", orderNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderNumber").value("ORD-123456"))
                .andExpect(jsonPath("$.customerName").value("John Doe"));

        verify(orderService).getOrderByOrderNumber(orderNumber);
    }

    @Test
    void updateOrderStatus_ShouldReturnUpdatedOrder() throws Exception {
        // Given
        Long orderId = 1L;
        Order.OrderStatus newStatus = Order.OrderStatus.CONFIRMED;
        OrderDto order = createOrderDto();
        order.setStatus(newStatus);
        when(orderService.updateOrderStatus(orderId, newStatus)).thenReturn(order);

        // When & Then
        mockMvc.perform(put("/api/orders/{orderId}/status", orderId)
                        .param("status", newStatus.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(orderService).updateOrderStatus(orderId, newStatus);
    }

    @Test
    void getOrdersByCustomerEmail_ShouldReturnOrders() throws Exception {
        // Given
        String email = "john@example.com";
        List<OrderDto> orders = Arrays.asList(
                createOrderDto(),
                createOrderDto2()
        );
        when(orderService.getOrdersByCustomerEmail(email)).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/api/orders/customer/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].customerEmail").value("john@example.com"))
                .andExpect(jsonPath("$[1].customerEmail").value("john@example.com"));

        verify(orderService).getOrdersByCustomerEmail(email);
    }

    @Test
    void getOrdersByStatus_ShouldReturnOrders() throws Exception {
        // Given
        Order.OrderStatus status = Order.OrderStatus.PENDING;
        List<OrderDto> orders = Arrays.asList(
                createOrderDto(),
                createOrderDto2()
        );
        when(orderService.getOrdersByStatus(status)).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/api/orders/status/{status}", status)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[1].status").value("PENDING"));

        verify(orderService).getOrdersByStatus(status);
    }

    private OrderDto createOrderDto() {
        OrderDto dto = new OrderDto();
        dto.setId(1L);
        dto.setOrderNumber("ORD-123456");
        dto.setCustomerName("John Doe");
        dto.setCustomerEmail("john@example.com");
        dto.setCustomerPhone("123-456-7890");
        dto.setTotalAmount(new BigDecimal("15.50"));
        dto.setStatus(Order.OrderStatus.PENDING);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        return dto;
    }

    private OrderDto createOrderDto2() {
        OrderDto dto = new OrderDto();
        dto.setId(2L);
        dto.setOrderNumber("ORD-123457");
        dto.setCustomerName("Jane Doe");
        dto.setCustomerEmail("john@example.com");
        dto.setCustomerPhone("987-654-3210");
        dto.setTotalAmount(new BigDecimal("12.75"));
        dto.setStatus(Order.OrderStatus.PENDING);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        return dto;
    }
}
