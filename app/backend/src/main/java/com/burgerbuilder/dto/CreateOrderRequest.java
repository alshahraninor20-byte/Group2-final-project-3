package com.burgerbuilder.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    
    @NotBlank(message = "Session ID is required")
    private String sessionId;
    
    @NotBlank(message = "Customer name is required")
    private String customerName;
    
    @Email(message = "Invalid email format")
    private String customerEmail;
    
    private String customerPhone;
    
    @NotEmpty(message = "Cart items cannot be empty")
    private List<Long> cartItemIds;
}
