package com.burgerbuilder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "burger_layers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BurgerLayer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id", nullable = false)
    private CartItem cartItem;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;
    
    @Min(value = 1, message = "Layer order must be at least 1")
    @Column(name = "layer_order", nullable = false)
    private Integer layerOrder;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;
    
    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    @PrePersist
    @PreUpdate
    protected void onPersist() {
        if (ingredient != null) {
            unitPrice = ingredient.getPrice();
        }
    }
}
