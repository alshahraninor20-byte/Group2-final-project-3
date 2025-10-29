package com.burgerbuilder.controller;

import com.burgerbuilder.dto.IngredientDto;
import com.burgerbuilder.service.IngredientService;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private IngredientController ingredientController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllIngredients_ShouldReturnListOfIngredients() throws Exception {
        // Given
        List<IngredientDto> ingredients = Arrays.asList(
                createIngredientDto(1L, "Beef Patty", "Protein", new BigDecimal("4.50")),
                createIngredientDto(2L, "Cheddar Cheese", "Cheese", new BigDecimal("1.25"))
        );
        when(ingredientService.getAllIngredients()).thenReturn(ingredients);

        // When & Then
        mockMvc.perform(get("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Beef Patty"))
                .andExpect(jsonPath("$[0].category").value("Protein"))
                .andExpect(jsonPath("$[0].price").value(4.50))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Cheddar Cheese"))
                .andExpect(jsonPath("$[1].category").value("Cheese"))
                .andExpect(jsonPath("$[1].price").value(1.25));
    }

    @Test
    void getIngredientsByCategory_ShouldReturnIngredientsForCategory() throws Exception {
        // Given
        String category = "Protein";
        List<IngredientDto> ingredients = Arrays.asList(
                createIngredientDto(1L, "Beef Patty", "Protein", new BigDecimal("4.50")),
                createIngredientDto(2L, "Chicken Breast", "Protein", new BigDecimal("4.25"))
        );
        when(ingredientService.getIngredientsByCategory(category)).thenReturn(ingredients);

        // When & Then
        mockMvc.perform(get("/api/ingredients/{category}", category)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].category").value("Protein"))
                .andExpect(jsonPath("$[1].category").value("Protein"));
    }

    @Test
    void getAllCategories_ShouldReturnListOfCategories() throws Exception {
        // Given
        List<String> categories = Arrays.asList("Bread", "Protein", "Cheese", "Vegetables", "Sauces");
        when(ingredientService.getAllCategories()).thenReturn(categories);

        // When & Then
        mockMvc.perform(get("/api/ingredients/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0]").value("Bread"))
                .andExpect(jsonPath("$[1]").value("Protein"))
                .andExpect(jsonPath("$[2]").value("Cheese"));
    }

    @Test
    void getIngredientById_ShouldReturnIngredient() throws Exception {
        // Given
        Long id = 1L;
        IngredientDto ingredient = createIngredientDto(id, "Beef Patty", "Protein", new BigDecimal("4.50"));
        when(ingredientService.getIngredientById(id)).thenReturn(ingredient);

        // When & Then
        mockMvc.perform(get("/api/ingredients/id/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Beef Patty"))
                .andExpect(jsonPath("$.category").value("Protein"))
                .andExpect(jsonPath("$.price").value(4.50));
    }

    @Test
    void getIngredientByName_ShouldReturnIngredient() throws Exception {
        // Given
        String name = "Beef Patty";
        IngredientDto ingredient = createIngredientDto(1L, name, "Protein", new BigDecimal("4.50"));
        when(ingredientService.getIngredientByName(name)).thenReturn(ingredient);

        // When & Then
        mockMvc.perform(get("/api/ingredients/name/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Beef Patty"))
                .andExpect(jsonPath("$.category").value("Protein"));
    }

    @Test
    void searchIngredients_ShouldReturnMatchingIngredients() throws Exception {
        // Given
        String searchTerm = "beef";
        List<IngredientDto> ingredients = Arrays.asList(
                createIngredientDto(1L, "Beef Patty", "Protein", new BigDecimal("4.50"))
        );
        when(ingredientService.searchIngredients(searchTerm)).thenReturn(ingredients);

        // When & Then
        mockMvc.perform(get("/api/ingredients/search")
                        .param("searchTerm", searchTerm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Beef Patty"));
    }

    private IngredientDto createIngredientDto(Long id, String name, String category, BigDecimal price) {
        IngredientDto dto = new IngredientDto();
        dto.setId(id);
        dto.setName(name);
        dto.setCategory(category);
        dto.setPrice(price);
        dto.setDescription("Test description");
        dto.setImageUrl("/images/test.jpg");
        dto.setIsAvailable(true);
        dto.setSortOrder(1);
        return dto;
    }
}
