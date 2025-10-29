package com.burgerbuilder.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BurgerLayerDto {
    
    private Long id;
    
    private Long cartItemId;
    
    @NotNull(message = "Ingredient ID is required")
    private Long ingredientId;
    
    private IngredientDto ingredient;
    
    @Min(value = 1, message = "Layer order must be at least 1")
    @NotNull(message = "Layer order is required")
    private Integer layerOrder;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity = 1;
    
    private BigDecimal unitPrice;
}
