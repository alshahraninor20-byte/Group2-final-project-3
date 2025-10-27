package com.burgerbuilder.service;

import com.burgerbuilder.dto.AddToCartRequest;
import com.burgerbuilder.dto.BurgerLayerDto;
import com.burgerbuilder.dto.CartItemDto;
import com.burgerbuilder.entity.BurgerLayer;
import com.burgerbuilder.entity.CartItem;
import com.burgerbuilder.entity.Ingredient;
import com.burgerbuilder.exception.CartException;
import com.burgerbuilder.exception.ResourceNotFoundException;
import com.burgerbuilder.repository.CartItemRepository;
import com.burgerbuilder.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartService {
    
    private final CartItemRepository cartItemRepository;
    private final IngredientService ingredientService;
    private final IngredientRepository ingredientRepository;
    
    public CartItemDto addToCart(AddToCartRequest request) {
        log.debug("Adding item to cart for session: {}, ingredient: {}", 
                 request.getSessionId(), request.getIngredientId());
        
        // Validate ingredient exists and is available
        Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "id", request.getIngredientId()));
        
        if (!ingredient.getIsAvailable()) {
            throw new CartException("Ingredient is not available");
        }
        
        // Check if item already exists in cart
        Optional<CartItem> existingItem = cartItemRepository
                .findBySessionIdAndIngredientId(request.getSessionId(), request.getIngredientId());
        
        CartItem cartItem;
        if (existingItem.isPresent()) {
            // Update quantity if item already exists
            cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            cartItem.setUpdatedAt(LocalDateTime.now());
        } else {
            // Create new cart item
            cartItem = new CartItem();
            cartItem.setSessionId(request.getSessionId());
            cartItem.setIngredient(ingredient);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setUnitPrice(ingredient.getPrice());
            cartItem.setCreatedAt(LocalDateTime.now());
            cartItem.setUpdatedAt(LocalDateTime.now());
        }
        
        // Calculate total price
        cartItem.setTotalPrice(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        
        // Save cart item
        cartItem = cartItemRepository.save(cartItem);
        
        // Add burger layers if provided
        if (request.getBurgerLayers() != null && !request.getBurgerLayers().isEmpty()) {
            addBurgerLayers(cartItem, request.getBurgerLayers());
        }
        
        log.info("Added item to cart successfully: cartItemId={}", cartItem.getId());
        return convertToDto(cartItem);
    }
    
    public List<CartItemDto> getCartBySession(String sessionId) {
        log.debug("Fetching cart for session: {}", sessionId);
        List<CartItem> cartItems = cartItemRepository.findBySessionIdOrderByCreatedAtDesc(sessionId);
        return cartItems.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public void removeCartItem(Long cartItemId, String sessionId) {
        log.debug("Removing cart item: {} for session: {}", cartItemId, sessionId);
        
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));
        
        if (!cartItem.getSessionId().equals(sessionId)) {
            throw new CartException("Cart item does not belong to this session");
        }
        
        cartItemRepository.delete(cartItem);
        log.info("Removed cart item successfully: cartItemId={}", cartItemId);
    }
    
    public void clearCart(String sessionId) {
        log.debug("Clearing cart for session: {}", sessionId);
        cartItemRepository.deleteBySessionId(sessionId);
        log.info("Cleared cart successfully for session: {}", sessionId);
    }
    
    public BigDecimal calculateTotalPrice(String sessionId) {
        log.debug("Calculating total price for session: {}", sessionId);
        BigDecimal total = cartItemRepository.getTotalPriceBySessionId(sessionId);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    public Long getCartItemCount(String sessionId) {
        log.debug("Getting cart item count for session: {}", sessionId);
        return cartItemRepository.countBySessionId(sessionId);
    }
    
    private void addBurgerLayers(CartItem cartItem, List<BurgerLayerDto> layerDtos) {
        for (BurgerLayerDto layerDto : layerDtos) {
            Ingredient layerIngredient = ingredientRepository.findById(layerDto.getIngredientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "id", layerDto.getIngredientId()));
            
            BurgerLayer layer = new BurgerLayer();
            layer.setCartItem(cartItem);
            layer.setIngredient(layerIngredient);
            layer.setLayerOrder(layerDto.getLayerOrder());
            layer.setQuantity(layerDto.getQuantity());
            layer.setUnitPrice(layerIngredient.getPrice());
        }
    }
    
    private CartItemDto convertToDto(CartItem cartItem) {
        CartItemDto dto = new CartItemDto();
        dto.setId(cartItem.getId());
        dto.setSessionId(cartItem.getSessionId());
        dto.setIngredientId(cartItem.getIngredient().getId());
        dto.setIngredient(ingredientService.convertToDto(cartItem.getIngredient()));
        dto.setQuantity(cartItem.getQuantity());
        dto.setUnitPrice(cartItem.getUnitPrice());
        dto.setTotalPrice(cartItem.getTotalPrice());
        dto.setCreatedAt(cartItem.getCreatedAt());
        dto.setUpdatedAt(cartItem.getUpdatedAt());
        
        // Convert burger layers
        if (cartItem.getBurgerLayers() != null) {
            dto.setBurgerLayers(cartItem.getBurgerLayers().stream()
                    .map(this::convertLayerToDto)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    private BurgerLayerDto convertLayerToDto(BurgerLayer layer) {
        BurgerLayerDto dto = new BurgerLayerDto();
        dto.setId(layer.getId());
        dto.setCartItemId(layer.getCartItem().getId());
        dto.setIngredientId(layer.getIngredient().getId());
        dto.setIngredient(ingredientService.convertToDto(layer.getIngredient()));
        dto.setLayerOrder(layer.getLayerOrder());
        dto.setQuantity(layer.getQuantity());
        dto.setUnitPrice(layer.getUnitPrice());
        return dto;
    }
}
