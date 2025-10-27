package com.burgerbuilder.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    
    @NotNull(message = "Session ID is required")
    private String sessionId;
    
    @NotNull(message = "Ingredient ID is required")
    private Long ingredientId;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    @NotNull(message = "Quantity is required")
    private Integer quantity = 1;
    
    private List<BurgerLayerDto> burgerLayers;
}
