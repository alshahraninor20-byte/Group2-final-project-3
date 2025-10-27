package com.burgerbuilder.repository;

import com.burgerbuilder.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {

    @Mock
    private OrderRepository orderRepository;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = createOrder("ORD-123456", "john@example.com", LocalDateTime.now());
    }

    @Test
    void findByOrderNumber_ShouldCallRepositoryMethod() {
        // Given
        String orderNumber = "ORD-123456";
        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(testOrder));

        // When
        Optional<Order> result = orderRepository.findByOrderNumber(orderNumber);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testOrder);
        verify(orderRepository).findByOrderNumber(orderNumber);
    }

    @Test
    void findByOrderNumber_WhenNotFound_ShouldReturnEmpty() {
        // Given
        String orderNumber = "ORD-NOTFOUND";
        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.empty());

        // When
        Optional<Order> result = orderRepository.findByOrderNumber(orderNumber);

        // Then
        assertThat(result).isEmpty();
        verify(orderRepository).findByOrderNumber(orderNumber);
    }

    @Test
    void findByCustomerEmailOrderByCreatedAtDesc_ShouldCallRepositoryMethod() {
        // Given
        String email = "john@example.com";
        List<Order> expectedOrders = Arrays.asList(testOrder);
        when(orderRepository.findByCustomerEmailOrderByCreatedAtDesc(email)).thenReturn(expectedOrders);

        // When
        List<Order> result = orderRepository.findByCustomerEmailOrderByCreatedAtDesc(email);

        // Then
        assertThat(result).isEqualTo(expectedOrders);
        verify(orderRepository).findByCustomerEmailOrderByCreatedAtDesc(email);
    }

    @Test
    void findByStatusOrderByCreatedAtDesc_ShouldCallRepositoryMethod() {
        // Given
        Order.OrderStatus status = Order.OrderStatus.PENDING;
        List<Order> expectedOrders = Arrays.asList(testOrder);
        when(orderRepository.findByStatusOrderByCreatedAtDesc(status)).thenReturn(expectedOrders);

        // When
        List<Order> result = orderRepository.findByStatusOrderByCreatedAtDesc(status);

        // Then
        assertThat(result).isEqualTo(expectedOrders);
        verify(orderRepository).findByStatusOrderByCreatedAtDesc(status);
    }

    @Test
    void findByDateRange_ShouldCallRepositoryMethod() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusHours(2);
        LocalDateTime endDate = LocalDateTime.now().plusHours(1);
        List<Order> expectedOrders = Arrays.asList(testOrder);
        when(orderRepository.findByDateRange(startDate, endDate)).thenReturn(expectedOrders);

        // When
        List<Order> result = orderRepository.findByDateRange(startDate, endDate);

        // Then
        assertThat(result).isEqualTo(expectedOrders);
        verify(orderRepository).findByDateRange(startDate, endDate);
    }

    @Test
    void findByCustomerNameContaining_ShouldCallRepositoryMethod() {
        // Given
        String customerName = "John";
        List<Order> expectedOrders = Arrays.asList(testOrder);
        when(orderRepository.findByCustomerNameContaining(customerName)).thenReturn(expectedOrders);

        // When
        List<Order> result = orderRepository.findByCustomerNameContaining(customerName);

        // Then
        assertThat(result).isEqualTo(expectedOrders);
        verify(orderRepository).findByCustomerNameContaining(customerName);
    }

    @Test
    void countByStatus_ShouldCallRepositoryMethod() {
        // Given
        Order.OrderStatus status = Order.OrderStatus.PENDING;
        Long expectedCount = 5L;
        when(orderRepository.countByStatus(status)).thenReturn(expectedCount);

        // When
        Long result = orderRepository.countByStatus(status);

        // Then
        assertThat(result).isEqualTo(expectedCount);
        verify(orderRepository).countByStatus(status);
    }

    @Test
    void getTotalRevenueByDateRange_ShouldCallRepositoryMethod() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusHours(2);
        LocalDateTime endDate = LocalDateTime.now().plusHours(1);
        BigDecimal expectedRevenue = new BigDecimal("150.75");
        when(orderRepository.getTotalRevenueByDateRange(startDate, endDate)).thenReturn(expectedRevenue);

        // When
        BigDecimal result = orderRepository.getTotalRevenueByDateRange(startDate, endDate);

        // Then
        assertThat(result).isEqualTo(expectedRevenue);
        verify(orderRepository).getTotalRevenueByDateRange(startDate, endDate);
    }

    @Test
    void findByCustomerEmailAndStatusIn_ShouldCallRepositoryMethod() {
        // Given
        String email = "john@example.com";
        List<Order.OrderStatus> statuses = Arrays.asList(
                Order.OrderStatus.PENDING,
                Order.OrderStatus.CONFIRMED
        );
        List<Order> expectedOrders = Arrays.asList(testOrder);
        when(orderRepository.findByCustomerEmailAndStatusIn(email, statuses)).thenReturn(expectedOrders);

        // When
        List<Order> result = orderRepository.findByCustomerEmailAndStatusIn(email, statuses);

        // Then
        assertThat(result).isEqualTo(expectedOrders);
        verify(orderRepository).findByCustomerEmailAndStatusIn(email, statuses);
    }

    private Order createOrder(String orderNumber, String email, LocalDateTime createdAt) {
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setCustomerName("Test Customer");
        order.setCustomerEmail(email);
        order.setCustomerPhone("123-456-7890");
        order.setTotalAmount(new BigDecimal("15.50"));
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreatedAt(createdAt);
        order.setUpdatedAt(createdAt);
        return order;
    }
}