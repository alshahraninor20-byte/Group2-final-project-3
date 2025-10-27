package com.burgerbuilder.repository;

import com.burgerbuilder.entity.CartItem;
import com.burgerbuilder.entity.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemRepositoryTest {

    @Mock
    private CartItemRepository cartItemRepository;

    private CartItem testCartItem;

    @BeforeEach
    void setUp() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Beef Patty");
        ingredient.setPrice(new BigDecimal("4.50"));

        testCartItem = createCartItem("session123", ingredient, 2, LocalDateTime.now());
    }

    @Test
    void findBySessionIdOrderByCreatedAtDesc_ShouldCallRepositoryMethod() {
        // Given
        String sessionId = "session123";
        List<CartItem> expectedCartItems = Arrays.asList(testCartItem);
        when(cartItemRepository.findBySessionIdOrderByCreatedAtDesc(sessionId)).thenReturn(expectedCartItems);

        // When
        List<CartItem> result = cartItemRepository.findBySessionIdOrderByCreatedAtDesc(sessionId);

        // Then
        assertThat(result).isEqualTo(expectedCartItems);
        verify(cartItemRepository).findBySessionIdOrderByCreatedAtDesc(sessionId);
    }

    @Test
    void findBySessionIdAndIngredientId_ShouldCallRepositoryMethod() {
        // Given
        String sessionId = "session123";
        Long ingredientId = 1L;
        when(cartItemRepository.findBySessionIdAndIngredientId(sessionId, ingredientId))
                .thenReturn(Optional.of(testCartItem));

        // When
        Optional<CartItem> result = cartItemRepository.findBySessionIdAndIngredientId(sessionId, ingredientId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testCartItem);
        verify(cartItemRepository).findBySessionIdAndIngredientId(sessionId, ingredientId);
    }

    @Test
    void findBySessionIdAndIngredientId_WhenNotFound_ShouldReturnEmpty() {
        // Given
        String sessionId = "session123";
        Long ingredientId = 999L;
        when(cartItemRepository.findBySessionIdAndIngredientId(sessionId, ingredientId))
                .thenReturn(Optional.empty());

        // When
        Optional<CartItem> result = cartItemRepository.findBySessionIdAndIngredientId(sessionId, ingredientId);

        // Then
        assertThat(result).isEmpty();
        verify(cartItemRepository).findBySessionIdAndIngredientId(sessionId, ingredientId);
    }

    @Test
    void findBySessionAndIngredient_ShouldCallRepositoryMethod() {
        // Given
        String sessionId = "session123";
        Long ingredientId = 1L;
        when(cartItemRepository.findBySessionAndIngredient(sessionId, ingredientId))
                .thenReturn(Optional.of(testCartItem));

        // When
        Optional<CartItem> result = cartItemRepository.findBySessionAndIngredient(sessionId, ingredientId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testCartItem);
        verify(cartItemRepository).findBySessionAndIngredient(sessionId, ingredientId);
    }

    @Test
    void countBySessionId_ShouldCallRepositoryMethod() {
        // Given
        String sessionId = "session123";
        Long expectedCount = 3L;
        when(cartItemRepository.countBySessionId(sessionId)).thenReturn(expectedCount);

        // When
        Long result = cartItemRepository.countBySessionId(sessionId);

        // Then
        assertThat(result).isEqualTo(expectedCount);
        verify(cartItemRepository).countBySessionId(sessionId);
    }

    @Test
    void getTotalPriceBySessionId_ShouldCallRepositoryMethod() {
        // Given
        String sessionId = "session123";
        BigDecimal expectedTotal = new BigDecimal("15.50");
        when(cartItemRepository.getTotalPriceBySessionId(sessionId)).thenReturn(expectedTotal);

        // When
        BigDecimal result = cartItemRepository.getTotalPriceBySessionId(sessionId);

        // Then
        assertThat(result).isEqualTo(expectedTotal);
        verify(cartItemRepository).getTotalPriceBySessionId(sessionId);
    }

    @Test
    void deleteBySessionId_ShouldCallRepositoryMethod() {
        // Given
        String sessionId = "session123";

        // When
        cartItemRepository.deleteBySessionId(sessionId);

        // Then
        verify(cartItemRepository).deleteBySessionId(sessionId);
    }

    @Test
    void deleteBySessionIdAndId_ShouldCallRepositoryMethod() {
        // Given
        String sessionId = "session123";
        Long cartItemId = 1L;

        // When
        cartItemRepository.deleteBySessionIdAndId(sessionId, cartItemId);

        // Then
        verify(cartItemRepository).deleteBySessionIdAndId(sessionId, cartItemId);
    }

    @Test
    void deleteExpiredCartItems_ShouldCallRepositoryMethod() {
        // Given
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);

        // When
        cartItemRepository.deleteExpiredCartItems(cutoffTime);

        // Then
        verify(cartItemRepository).deleteExpiredCartItems(cutoffTime);
    }

    private CartItem createCartItem(String sessionId, Ingredient ingredient, Integer quantity, LocalDateTime createdAt) {
        CartItem cartItem = new CartItem();
        cartItem.setSessionId(sessionId);
        cartItem.setIngredient(ingredient);
        cartItem.setQuantity(quantity);
        cartItem.setUnitPrice(ingredient.getPrice());
        cartItem.setTotalPrice(ingredient.getPrice().multiply(BigDecimal.valueOf(quantity)));
        cartItem.setCreatedAt(createdAt);
        cartItem.setUpdatedAt(createdAt);
        return cartItem;
    }
}