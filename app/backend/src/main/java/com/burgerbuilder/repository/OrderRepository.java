package com.burgerbuilder.repository;

import com.burgerbuilder.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    List<Order> findByCustomerEmailOrderByCreatedAtDesc(String customerEmail);
    
    List<Order> findByStatusOrderByCreatedAtDesc(Order.OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.customerName LIKE %:customerName% ORDER BY o.createdAt DESC")
    List<Order> findByCustomerNameContaining(@Param("customerName") String customerName);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    Long countByStatus(@Param("status") Order.OrderStatus status);
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    java.math.BigDecimal getTotalRevenueByDateRange(@Param("startDate") LocalDateTime startDate, 
                                                    @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.customerEmail = :email AND o.status IN :statuses ORDER BY o.createdAt DESC")
    List<Order> findByCustomerEmailAndStatusIn(@Param("email") String email, 
                                               @Param("statuses") List<Order.OrderStatus> statuses);
}
