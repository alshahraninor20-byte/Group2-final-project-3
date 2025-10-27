package com.burgerbuilder.controller;

import com.burgerbuilder.dto.AddToCartRequest;
import com.burgerbuilder.dto.CartItemDto;
import com.burgerbuilder.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    
    private final CartService cartService;
    
    @PostMapping("/items")
    public ResponseEntity<CartItemDto> addToCart(@Valid @RequestBody AddToCartRequest request) {
        log.debug("POST /api/cart/items - Adding item to cart for session: {}", request.getSessionId());
        CartItemDto cartItem = cartService.addToCart(request);
        return ResponseEntity.ok(cartItem);
    }
    
    @GetMapping("/{sessionId}")
    public ResponseEntity<List<CartItemDto>> getCartBySession(@PathVariable String sessionId) {
        log.debug("GET /api/cart/{} - Fetching cart for session", sessionId);
        List<CartItemDto> cartItems = cartService.getCartBySession(sessionId);
        return ResponseEntity.ok(cartItems);
    }
    
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeCartItem(
            @PathVariable Long itemId,
            @RequestParam String sessionId) {
        log.debug("DELETE /api/cart/items/{} - Removing cart item for session: {}", itemId, sessionId);
        cartService.removeCartItem(itemId, sessionId);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> clearCart(@PathVariable String sessionId) {
        log.debug("DELETE /api/cart/{} - Clearing cart for session", sessionId);
        cartService.clearCart(sessionId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{sessionId}/total")
    public ResponseEntity<BigDecimal> getCartTotal(@PathVariable String sessionId) {
        log.debug("GET /api/cart/{}/total - Getting cart total for session", sessionId);
        BigDecimal total = cartService.calculateTotalPrice(sessionId);
        return ResponseEntity.ok(total);
    }
    
    @GetMapping("/{sessionId}/count")
    public ResponseEntity<Long> getCartItemCount(@PathVariable String sessionId) {
        log.debug("GET /api/cart/{}/count - Getting cart item count for session", sessionId);
        Long count = cartService.getCartItemCount(sessionId);
        return ResponseEntity.ok(count);
    }
    
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }
}
