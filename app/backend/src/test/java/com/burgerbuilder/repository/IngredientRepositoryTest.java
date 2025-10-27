package com.burgerbuilder.repository;

import com.burgerbuilder.entity.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientRepositoryTest {

    @Mock
    private IngredientRepository ingredientRepository;

    private Ingredient testIngredient;

    @BeforeEach
    void setUp() {
        testIngredient = createIngredient("Beef Patty", "Protein", new BigDecimal("4.50"), 1);
    }

    @Test
    void findByCategoryOrderBySortOrderAsc_ShouldCallRepositoryMethod() {
        // Given
        String category = "Protein";
        List<Ingredient> expectedIngredients = Arrays.asList(testIngredient);
        when(ingredientRepository.findByCategoryOrderBySortOrderAsc(category)).thenReturn(expectedIngredients);

        // When
        List<Ingredient> result = ingredientRepository.findByCategoryOrderBySortOrderAsc(category);

        // Then
        assertThat(result).isEqualTo(expectedIngredients);
        verify(ingredientRepository).findByCategoryOrderBySortOrderAsc(category);
    }

    @Test
    void findByIsAvailableTrueOrderBySortOrderAsc_ShouldCallRepositoryMethod() {
        // Given
        List<Ingredient> expectedIngredients = Arrays.asList(testIngredient);
        when(ingredientRepository.findByIsAvailableTrueOrderBySortOrderAsc()).thenReturn(expectedIngredients);

        // When
        List<Ingredient> result = ingredientRepository.findByIsAvailableTrueOrderBySortOrderAsc();

        // Then
        assertThat(result).isEqualTo(expectedIngredients);
        verify(ingredientRepository).findByIsAvailableTrueOrderBySortOrderAsc();
    }

    @Test
    void findByCategoryAndIsAvailableTrueOrderBySortOrderAsc_ShouldCallRepositoryMethod() {
        // Given
        String category = "Protein";
        List<Ingredient> expectedIngredients = Arrays.asList(testIngredient);
        when(ingredientRepository.findByCategoryAndIsAvailableTrueOrderBySortOrderAsc(category))
                .thenReturn(expectedIngredients);

        // When
        List<Ingredient> result = ingredientRepository.findByCategoryAndIsAvailableTrueOrderBySortOrderAsc(category);

        // Then
        assertThat(result).isEqualTo(expectedIngredients);
        verify(ingredientRepository).findByCategoryAndIsAvailableTrueOrderBySortOrderAsc(category);
    }

    @Test
    void findByName_ShouldCallRepositoryMethod() {
        // Given
        String name = "Beef Patty";
        when(ingredientRepository.findByName(name)).thenReturn(Optional.of(testIngredient));

        // When
        Optional<Ingredient> result = ingredientRepository.findByName(name);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testIngredient);
        verify(ingredientRepository).findByName(name);
    }

    @Test
    void findByName_WhenNotFound_ShouldReturnEmpty() {
        // Given
        String name = "Non-existent";
        when(ingredientRepository.findByName(name)).thenReturn(Optional.empty());

        // When
        Optional<Ingredient> result = ingredientRepository.findByName(name);

        // Then
        assertThat(result).isEmpty();
        verify(ingredientRepository).findByName(name);
    }

    @Test
    void findDistinctCategories_ShouldCallRepositoryMethod() {
        // Given
        List<String> expectedCategories = Arrays.asList("Protein", "Bread", "Cheese");
        when(ingredientRepository.findDistinctCategories()).thenReturn(expectedCategories);

        // When
        List<String> result = ingredientRepository.findDistinctCategories();

        // Then
        assertThat(result).isEqualTo(expectedCategories);
        verify(ingredientRepository).findDistinctCategories();
    }

    @Test
    void searchIngredients_ShouldCallRepositoryMethod() {
        // Given
        String searchTerm = "beef";
        List<Ingredient> expectedIngredients = Arrays.asList(testIngredient);
        when(ingredientRepository.searchIngredients(searchTerm)).thenReturn(expectedIngredients);

        // When
        List<Ingredient> result = ingredientRepository.searchIngredients(searchTerm);

        // Then
        assertThat(result).isEqualTo(expectedIngredients);
        verify(ingredientRepository).searchIngredients(searchTerm);
    }

    private Ingredient createIngredient(String name, String category, BigDecimal price, Integer sortOrder) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setCategory(category);
        ingredient.setPrice(price);
        ingredient.setDescription("Test description");
        ingredient.setImageUrl("/images/test.jpg");
        ingredient.setIsAvailable(true);
        ingredient.setSortOrder(sortOrder);
        return ingredient;
    }
}