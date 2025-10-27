package com.burgerbuilder.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    
    private Long id;
    
    private Long orderId;
    
    @NotNull(message = "Ingredient ID is required")
    private Long ingredientId;
    
    private IngredientDto ingredient;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    @NotNull(message = "Quantity is required")
    private Integer quantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal totalPrice;
    
    private List<OrderLayerDto> orderLayers;
}
