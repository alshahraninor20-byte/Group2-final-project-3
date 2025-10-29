package com.burgerbuilder.service;

import com.burgerbuilder.dto.AddToCartRequest;
import com.burgerbuilder.dto.CartItemDto;
import com.burgerbuilder.entity.CartItem;
import com.burgerbuilder.entity.Ingredient;
import com.burgerbuilder.exception.CartException;
import com.burgerbuilder.exception.ResourceNotFoundException;
import com.burgerbuilder.repository.CartItemRepository;
import com.burgerbuilder.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private CartService cartService;

    private Ingredient testIngredient;
    private CartItem testCartItem;
    private AddToCartRequest testRequest;

    @BeforeEach
    void setUp() {
        testIngredient = new Ingredient();
        testIngredient.setId(1L);
        testIngredient.setName("Beef Patty");
        testIngredient.setCategory("Protein");
        testIngredient.setPrice(new BigDecimal("4.50"));
        testIngredient.setIsAvailable(true);

        testCartItem = new CartItem();
        testCartItem.setId(1L);
        testCartItem.setSessionId("session123");
        testCartItem.setIngredient(testIngredient);
        testCartItem.setQuantity(2);
        testCartItem.setUnitPrice(new BigDecimal("4.50"));
        testCartItem.setTotalPrice(new BigDecimal("9.00"));
        testCartItem.setCreatedAt(LocalDateTime.now());
        testCartItem.setUpdatedAt(LocalDateTime.now());

        testRequest = new AddToCartRequest();
        testRequest.setSessionId("session123");
        testRequest.setIngredientId(1L);
        testRequest.setQuantity(2);
    }

    @Test
    void addToCart_WithNewItem_ShouldCreateNewCartItem() {
        // Given
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(testIngredient));
        when(cartItemRepository.findBySessionIdAndIngredientId("session123", 1L))
                .thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);

        // When
        CartItemDto result = cartService.addToCart(testRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getSessionId()).isEqualTo("session123");
        assertThat(result.getQuantity()).isEqualTo(2);
        assertThat(result.getTotalPrice()).isEqualTo(new BigDecimal("9.00"));
        verify(ingredientRepository).findById(1L);
        verify(cartItemRepository).findBySessionIdAndIngredientId("session123", 1L);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void addToCart_WithExistingItem_ShouldUpdateQuantity() {
        // Given
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(testIngredient));
        when(cartItemRepository.findBySessionIdAndIngredientId("session123", 1L))
                .thenReturn(Optional.of(testCartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);

        // When
        CartItemDto result = cartService.addToCart(testRequest);

        // Then
        assertThat(result).isNotNull();
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void addToCart_WhenIngredientNotFound_ShouldThrowException() {
        // Given
        when(ingredientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> cartService.addToCart(testRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Ingredient not found with id: 1");
    }

    @Test
    void addToCart_WhenIngredientNotAvailable_ShouldThrowException() {
        // Given
        testIngredient.setIsAvailable(false);
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(testIngredient));

        // When & Then
        assertThatThrownBy(() -> cartService.addToCart(testRequest))
                .isInstanceOf(CartException.class)
                .hasMessage("Ingredient is not available");
    }

    @Test
    void getCartBySession_ShouldReturnCartItems() {
        // Given
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        when(cartItemRepository.findBySessionIdOrderByCreatedAtDesc("session123"))
                .thenReturn(cartItems);

        // When
        List<CartItemDto> result = cartService.getCartBySession("session123");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSessionId()).isEqualTo("session123");
        verify(cartItemRepository).findBySessionIdOrderByCreatedAtDesc("session123");
    }

    @Test
    void removeCartItem_ShouldDeleteCartItem() {
        // Given
        Long cartItemId = 1L;
        String sessionId = "session123";
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(testCartItem));

        // When
        cartService.removeCartItem(cartItemId, sessionId);

        // Then
        verify(cartItemRepository).findById(cartItemId);
        verify(cartItemRepository).delete(testCartItem);
    }

    @Test
    void removeCartItem_WhenCartItemNotFound_ShouldThrowException() {
        // Given
        Long cartItemId = 999L;
        String sessionId = "session123";
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> cartService.removeCartItem(cartItemId, sessionId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("CartItem not found with id: 999");
    }

    @Test
    void removeCartItem_WhenSessionMismatch_ShouldThrowException() {
        // Given
        Long cartItemId = 1L;
        String sessionId = "different-session";
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(testCartItem));

        // When & Then
        assertThatThrownBy(() -> cartService.removeCartItem(cartItemId, sessionId))
                .isInstanceOf(CartException.class)
                .hasMessage("Cart item does not belong to this session");
    }

    @Test
    void clearCart_ShouldDeleteAllCartItems() {
        // Given
        String sessionId = "session123";

        // When
        cartService.clearCart(sessionId);

        // Then
        verify(cartItemRepository).deleteBySessionId(sessionId);
    }

    @Test
    void calculateTotalPrice_ShouldReturnTotal() {
        // Given
        String sessionId = "session123";
        BigDecimal expectedTotal = new BigDecimal("15.50");
        when(cartItemRepository.getTotalPriceBySessionId(sessionId)).thenReturn(expectedTotal);

        // When
        BigDecimal result = cartService.calculateTotalPrice(sessionId);

        // Then
        assertThat(result).isEqualTo(expectedTotal);
        verify(cartItemRepository).getTotalPriceBySessionId(sessionId);
    }

    @Test
    void calculateTotalPrice_WhenNoItems_ShouldReturnZero() {
        // Given
        String sessionId = "session123";
        when(cartItemRepository.getTotalPriceBySessionId(sessionId)).thenReturn(null);

        // When
        BigDecimal result = cartService.calculateTotalPrice(sessionId);

        // Then
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void getCartItemCount_ShouldReturnCount() {
        // Given
        String sessionId = "session123";
        Long expectedCount = 3L;
        when(cartItemRepository.countBySessionId(sessionId)).thenReturn(expectedCount);

        // When
        Long result = cartService.getCartItemCount(sessionId);

        // Then
        assertThat(result).isEqualTo(expectedCount);
        verify(cartItemRepository).countBySessionId(sessionId);
    }
}
