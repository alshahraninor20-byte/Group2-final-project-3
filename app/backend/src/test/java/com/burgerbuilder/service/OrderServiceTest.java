package com.burgerbuilder.service;

import com.burgerbuilder.dto.CreateOrderRequest;
import com.burgerbuilder.dto.OrderDto;
import com.burgerbuilder.entity.*;
import com.burgerbuilder.exception.OrderException;
import com.burgerbuilder.exception.ResourceNotFoundException;
import com.burgerbuilder.repository.CartItemRepository;
import com.burgerbuilder.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private OrderService orderService;

    private CreateOrderRequest testRequest;
    private CartItem testCartItem;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        Ingredient testIngredient = new Ingredient();
        testIngredient.setId(1L);
        testIngredient.setName("Beef Patty");
        testIngredient.setPrice(new BigDecimal("4.50"));

        testCartItem = new CartItem();
        testCartItem.setId(1L);
        testCartItem.setSessionId("session123");
        testCartItem.setIngredient(testIngredient);
        testCartItem.setQuantity(2);
        testCartItem.setUnitPrice(new BigDecimal("4.50"));
        testCartItem.setTotalPrice(new BigDecimal("9.00"));

        testRequest = new CreateOrderRequest();
        testRequest.setSessionId("session123");
        testRequest.setCustomerName("John Doe");
        testRequest.setCustomerEmail("john@example.com");
        testRequest.setCartItemIds(Arrays.asList(1L, 2L));

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderNumber("ORD-123456");
        testOrder.setCustomerName("John Doe");
        testOrder.setCustomerEmail("john@example.com");
        testOrder.setTotalAmount(new BigDecimal("15.50"));
        testOrder.setStatus(Order.OrderStatus.PENDING);
        testOrder.setCreatedAt(LocalDateTime.now());
        testOrder.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createOrderFromCart_ShouldCreateOrderSuccessfully() {
        // Given
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));
        when(cartItemRepository.findById(2L)).thenReturn(Optional.of(testCartItem));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        OrderDto result = orderService.createOrderFromCart(testRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCustomerName()).isEqualTo("John Doe");
        assertThat(result.getCustomerEmail()).isEqualTo("john@example.com");
        assertThat(result.getStatus()).isEqualTo(Order.OrderStatus.PENDING);
        verify(orderRepository).save(any(Order.class));
        verify(cartItemRepository).deleteBySessionId("session123");
    }

    @Test
    void createOrderFromCart_WhenCartIsEmpty_ShouldThrowException() {
        // Given
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.createOrderFromCart(testRequest))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createOrderFromCart_WhenCartItemNotBelongsToSession_ShouldThrowException() {
        // Given
        testCartItem.setSessionId("different-session");
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));

        // When & Then
        assertThatThrownBy(() -> orderService.createOrderFromCart(testRequest))
                .isInstanceOf(OrderException.class)
                .hasMessage("Cart item does not belong to this session");
    }

    @Test
    void getOrderById_WhenOrderExists_ShouldReturnOrder() {
        // Given
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));

        // When
        OrderDto result = orderService.getOrderById(orderId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getOrderNumber()).isEqualTo("ORD-123456");
        assertThat(result.getCustomerName()).isEqualTo("John Doe");
        verify(orderRepository).findById(orderId);
    }

    @Test
    void getOrderById_WhenOrderNotFound_ShouldThrowException() {
        // Given
        Long orderId = 999L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.getOrderById(orderId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Order not found with id: 999");
    }

    @Test
    void getOrderByOrderNumber_WhenOrderExists_ShouldReturnOrder() {
        // Given
        String orderNumber = "ORD-123456";
        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(testOrder));

        // When
        OrderDto result = orderService.getOrderByOrderNumber(orderNumber);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getOrderNumber()).isEqualTo("ORD-123456");
        assertThat(result.getCustomerName()).isEqualTo("John Doe");
        verify(orderRepository).findByOrderNumber(orderNumber);
    }

    @Test
    void getOrderByOrderNumber_WhenOrderNotFound_ShouldThrowException() {
        // Given
        String orderNumber = "ORD-NOTFOUND";
        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.getOrderByOrderNumber(orderNumber))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Order not found with orderNumber: ORD-NOTFOUND");
    }

    @Test
    void updateOrderStatus_ShouldUpdateStatusSuccessfully() {
        // Given
        Long orderId = 1L;
        Order.OrderStatus newStatus = Order.OrderStatus.CONFIRMED;
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        OrderDto result = orderService.updateOrderStatus(orderId, newStatus);

        // Then
        assertThat(result).isNotNull();
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void updateOrderStatus_WhenOrderNotFound_ShouldThrowException() {
        // Given
        Long orderId = 999L;
        Order.OrderStatus newStatus = Order.OrderStatus.CONFIRMED;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrderStatus(orderId, newStatus))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Order not found with id: 999");
    }

    @Test
    void getOrdersByCustomerEmail_ShouldReturnOrders() {
        // Given
        String email = "john@example.com";
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByCustomerEmailOrderByCreatedAtDesc(email)).thenReturn(orders);

        // When
        List<OrderDto> result = orderService.getOrdersByCustomerEmail(email);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustomerEmail()).isEqualTo("john@example.com");
        verify(orderRepository).findByCustomerEmailOrderByCreatedAtDesc(email);
    }

    @Test
    void getOrdersByStatus_ShouldReturnOrders() {
        // Given
        Order.OrderStatus status = Order.OrderStatus.PENDING;
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByStatusOrderByCreatedAtDesc(status)).thenReturn(orders);

        // When
        List<OrderDto> result = orderService.getOrdersByStatus(status);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(Order.OrderStatus.PENDING);
        verify(orderRepository).findByStatusOrderByCreatedAtDesc(status);
    }
}
