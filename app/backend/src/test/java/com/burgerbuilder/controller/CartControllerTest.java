package com.burgerbuilder.controller;

import com.burgerbuilder.dto.AddToCartRequest;
import com.burgerbuilder.dto.CartItemDto;
import com.burgerbuilder.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void addToCart_ShouldReturnCartItem() throws Exception {
        // Given
        AddToCartRequest request = new AddToCartRequest();
        request.setSessionId("session123");
        request.setIngredientId(1L);
        request.setQuantity(2);

        CartItemDto cartItem = createCartItemDto();
        when(cartService.addToCart(any(AddToCartRequest.class))).thenReturn(cartItem);

        // When & Then
        mockMvc.perform(post("/api/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sessionId").value("session123"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.totalPrice").value(9.00));

        verify(cartService).addToCart(any(AddToCartRequest.class));
    }

    @Test
    void addToCart_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Given
        AddToCartRequest request = new AddToCartRequest();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/api/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCartBySession_ShouldReturnCartItems() throws Exception {
        // Given
        String sessionId = "session123";
        List<CartItemDto> cartItems = Arrays.asList(
                createCartItemDto(),
                createCartItemDto2()
        );
        when(cartService.getCartBySession(sessionId)).thenReturn(cartItems);

        // When & Then
        mockMvc.perform(get("/api/cart/{sessionId}", sessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].sessionId").value("session123"))
                .andExpect(jsonPath("$[1].sessionId").value("session123"));

        verify(cartService).getCartBySession(sessionId);
    }

    @Test
    void removeCartItem_ShouldReturnNoContent() throws Exception {
        // Given
        Long itemId = 1L;
        String sessionId = "session123";

        // When & Then
        mockMvc.perform(delete("/api/cart/items/{itemId}", itemId)
                        .param("sessionId", sessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(cartService).removeCartItem(itemId, sessionId);
    }

    @Test
    void clearCart_ShouldReturnNoContent() throws Exception {
        // Given
        String sessionId = "session123";

        // When & Then
        mockMvc.perform(delete("/api/cart/{sessionId}", sessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(cartService).clearCart(sessionId);
    }

    @Test
    void getCartTotal_ShouldReturnTotalPrice() throws Exception {
        // Given
        String sessionId = "session123";
        BigDecimal total = new BigDecimal("15.50");
        when(cartService.calculateTotalPrice(sessionId)).thenReturn(total);

        // When & Then
        mockMvc.perform(get("/api/cart/{sessionId}/total", sessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(15.50));

        verify(cartService).calculateTotalPrice(sessionId);
    }

    @Test
    void getCartItemCount_ShouldReturnCount() throws Exception {
        // Given
        String sessionId = "session123";
        Long count = 3L;
        when(cartService.getCartItemCount(sessionId)).thenReturn(count);

        // When & Then
        mockMvc.perform(get("/api/cart/{sessionId}/count", sessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(3));

        verify(cartService).getCartItemCount(sessionId);
    }

    private CartItemDto createCartItemDto() {
        CartItemDto dto = new CartItemDto();
        dto.setId(1L);
        dto.setSessionId("session123");
        dto.setIngredientId(1L);
        dto.setQuantity(2);
        dto.setUnitPrice(new BigDecimal("4.50"));
        dto.setTotalPrice(new BigDecimal("9.00"));
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        return dto;
    }

    private CartItemDto createCartItemDto2() {
        CartItemDto dto = new CartItemDto();
        dto.setId(2L);
        dto.setSessionId("session123");
        dto.setIngredientId(2L);
        dto.setQuantity(1);
        dto.setUnitPrice(new BigDecimal("1.25"));
        dto.setTotalPrice(new BigDecimal("1.25"));
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        return dto;
    }
}
