package com.burgerbuilder.service;

import com.burgerbuilder.dto.IngredientDto;
import com.burgerbuilder.entity.Ingredient;
import com.burgerbuilder.exception.ResourceNotFoundException;
import com.burgerbuilder.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientService ingredientService;

    private Ingredient testIngredient;

    @BeforeEach
    void setUp() {
        testIngredient = new Ingredient();
        testIngredient.setId(1L);
        testIngredient.setName("Beef Patty");
        testIngredient.setCategory("Protein");
        testIngredient.setPrice(new BigDecimal("4.50"));
        testIngredient.setDescription("Fresh beef patty");
        testIngredient.setImageUrl("/images/beef-patty.jpg");
        testIngredient.setIsAvailable(true);
        testIngredient.setSortOrder(1);
    }

    @Test
    void getAllIngredients_ShouldReturnAvailableIngredients() {
        // Given
        List<Ingredient> ingredients = Arrays.asList(testIngredient);
        when(ingredientRepository.findByIsAvailableTrueOrderBySortOrderAsc()).thenReturn(ingredients);

        // When
        List<IngredientDto> result = ingredientService.getAllIngredients();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Beef Patty");
        assertThat(result.get(0).getCategory()).isEqualTo("Protein");
        assertThat(result.get(0).getPrice()).isEqualTo(new BigDecimal("4.50"));
        verify(ingredientRepository).findByIsAvailableTrueOrderBySortOrderAsc();
    }

    @Test
    void getIngredientsByCategory_ShouldReturnIngredientsForCategory() {
        // Given
        String category = "Protein";
        List<Ingredient> ingredients = Arrays.asList(testIngredient);
        when(ingredientRepository.findByCategoryAndIsAvailableTrueOrderBySortOrderAsc(category))
                .thenReturn(ingredients);

        // When
        List<IngredientDto> result = ingredientService.getIngredientsByCategory(category);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo("Protein");
        verify(ingredientRepository).findByCategoryAndIsAvailableTrueOrderBySortOrderAsc(category);
    }

    @Test
    void getAllCategories_ShouldReturnDistinctCategories() {
        // Given
        List<String> categories = Arrays.asList("Bread", "Protein", "Cheese", "Vegetables", "Sauces");
        when(ingredientRepository.findDistinctCategories()).thenReturn(categories);

        // When
        List<String> result = ingredientService.getAllCategories();

        // Then
        assertThat(result).hasSize(5);
        assertThat(result).containsExactly("Bread", "Protein", "Cheese", "Vegetables", "Sauces");
        verify(ingredientRepository).findDistinctCategories();
    }

    @Test
    void getIngredientById_WhenIngredientExists_ShouldReturnIngredient() {
        // Given
        Long id = 1L;
        when(ingredientRepository.findById(id)).thenReturn(Optional.of(testIngredient));

        // When
        IngredientDto result = ingredientService.getIngredientById(id);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Beef Patty");
        assertThat(result.getCategory()).isEqualTo("Protein");
        verify(ingredientRepository).findById(id);
    }

    @Test
    void getIngredientById_WhenIngredientNotFound_ShouldThrowException() {
        // Given
        Long id = 999L;
        when(ingredientRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> ingredientService.getIngredientById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Ingredient not found with id: 999");
        verify(ingredientRepository).findById(id);
    }

    @Test
    void getIngredientById_WhenIngredientNotAvailable_ShouldThrowException() {
        // Given
        Long id = 1L;
        testIngredient.setIsAvailable(false);
        when(ingredientRepository.findById(id)).thenReturn(Optional.of(testIngredient));

        // When & Then
        assertThatThrownBy(() -> ingredientService.getIngredientById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Ingredient not found with id: 1");
    }

    @Test
    void getIngredientByName_WhenIngredientExists_ShouldReturnIngredient() {
        // Given
        String name = "Beef Patty";
        when(ingredientRepository.findByName(name)).thenReturn(Optional.of(testIngredient));

        // When
        IngredientDto result = ingredientService.getIngredientByName(name);

        // Then
        assertThat(result.getName()).isEqualTo("Beef Patty");
        assertThat(result.getCategory()).isEqualTo("Protein");
        verify(ingredientRepository).findByName(name);
    }

    @Test
    void getIngredientByName_WhenIngredientNotFound_ShouldThrowException() {
        // Given
        String name = "Non-existent Ingredient";
        when(ingredientRepository.findByName(name)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> ingredientService.getIngredientByName(name))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Ingredient not found with name: Non-existent Ingredient");
    }

    @Test
    void getIngredientByName_WhenIngredientNotAvailable_ShouldThrowException() {
        // Given
        String name = "Beef Patty";
        testIngredient.setIsAvailable(false);
        when(ingredientRepository.findByName(name)).thenReturn(Optional.of(testIngredient));

        // When & Then
        assertThatThrownBy(() -> ingredientService.getIngredientByName(name))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Ingredient not found with name: Beef Patty");
    }

    @Test
    void searchIngredients_ShouldReturnMatchingIngredients() {
        // Given
        String searchTerm = "beef";
        List<Ingredient> ingredients = Arrays.asList(testIngredient);
        when(ingredientRepository.searchIngredients(searchTerm)).thenReturn(ingredients);

        // When
        List<IngredientDto> result = ingredientService.searchIngredients(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Beef Patty");
        verify(ingredientRepository).searchIngredients(searchTerm);
    }

    @Test
    void convertToDto_ShouldConvertEntityToDto() {
        // Given
        testIngredient.setId(1L);
        testIngredient.setName("Test Ingredient");
        testIngredient.setCategory("Test Category");
        testIngredient.setPrice(new BigDecimal("5.99"));
        testIngredient.setDescription("Test Description");
        testIngredient.setImageUrl("/test.jpg");
        testIngredient.setIsAvailable(true);
        testIngredient.setSortOrder(10);

        // When
        IngredientDto result = ingredientService.convertToDto(testIngredient);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Ingredient");
        assertThat(result.getCategory()).isEqualTo("Test Category");
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("5.99"));
        assertThat(result.getDescription()).isEqualTo("Test Description");
        assertThat(result.getImageUrl()).isEqualTo("/test.jpg");
        assertThat(result.getIsAvailable()).isTrue();
        assertThat(result.getSortOrder()).isEqualTo(10);
    }
}
