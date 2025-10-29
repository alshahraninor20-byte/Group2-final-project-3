package com.burgerbuilder.dto;

import com.burgerbuilder.entity.Order;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    
    private Long id;
    
    private String orderNumber;
    
    @NotBlank(message = "Customer name is required")
    private String customerName;
    
    @Email(message = "Invalid email format")
    private String customerEmail;
    
    private String customerPhone;
    
    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;
    
    private Order.OrderStatus status;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private List<OrderItemDto> orderItems;
}
