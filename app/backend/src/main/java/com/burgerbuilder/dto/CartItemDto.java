package com.burgerbuilder.dto;

import jakarta.validation.constraints.Min;
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
public class CartItemDto {
    
    private Long id;
    
    @NotNull(message = "Session ID is required")
    private String sessionId;
    
    @NotNull(message = "Ingredient ID is required")
    private Long ingredientId;
    
    private IngredientDto ingredient;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    @NotNull(message = "Quantity is required")
    private Integer quantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal totalPrice;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private List<BurgerLayerDto> burgerLayers;
}
