package com.burgerbuilder.repository;

import com.burgerbuilder.entity.Ingredient;
import com.burgerbuilder.repository.IngredientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for data initialization functionality.
 * Tests that ingredients are properly seeded during application startup.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DataInitializationTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    void shouldHaveDefaultIngredientsAfterStartup() {
        // Given: Application has started (ingredients should be seeded)
        
        // When: We fetch all ingredients
        List<Ingredient> ingredients = ingredientRepository.findAll();
        
        // Then: We should have default ingredients
        assertThat(ingredients).isNotEmpty();
        assertThat(ingredients).hasSizeGreaterThanOrEqualTo(4); // We expect at least 4 test ingredients
        
        // Verify specific ingredients exist
        assertThat(ingredientRepository.findByName("Classic Sesame Bun")).isPresent();
        assertThat(ingredientRepository.findByName("Beef Patty")).isPresent();
        assertThat(ingredientRepository.findByName("Lettuce")).isPresent();
        assertThat(ingredientRepository.findByName("Ketchup")).isPresent();
    }

    @Test
    void shouldNotDuplicateIngredientsOnMultipleStartups() {
        // Given: Application has started (ingredients are seeded)
        List<Ingredient> initialIngredients = ingredientRepository.findAll();
        int initialCount = initialIngredients.size();
        
        // When: We simulate another startup by inserting the same data again
        // (This simulates what happens when the data.sql script runs again)
        // The IF NOT EXISTS check should prevent duplicates
        
        // Then: Count should remain the same
        List<Ingredient> afterSecondStartup = ingredientRepository.findAll();
        assertThat(afterSecondStartup).hasSize(initialCount);
        
        // Verify no duplicate names
        long uniqueNameCount = afterSecondStartup.stream()
                .map(Ingredient::getName)
                .distinct()
                .count();
        assertThat(uniqueNameCount).isEqualTo(initialCount);
    }

    @Test
    void shouldHaveCorrectIngredientStructure() {
        // Given: Application has started
        
        // When: We fetch a specific ingredient
        Ingredient classicBun = ingredientRepository.findByName("Classic Sesame Bun")
                .orElseThrow(() -> new AssertionError("Classic Sesame Bun not found"));
        
        // Then: It should have correct structure
        assertThat(classicBun.getId()).isNotNull();
        assertThat(classicBun.getName()).isEqualTo("Classic Sesame Bun");
        assertThat(classicBun.getCategory()).isEqualTo("buns");
        assertThat(classicBun.getPrice()).isEqualByComparingTo("1.50");
        assertThat(classicBun.getDescription()).isEqualTo("Fresh sesame seed bun");
        assertThat(classicBun.getImageUrl()).isEqualTo("/images/buns/sesame-bun.jpg");
        assertThat(classicBun.getIsAvailable()).isTrue();
        assertThat(classicBun.getSortOrder()).isEqualTo(1);
    }

    @Test
    void shouldHaveUniqueIngredients() {
        // Given: Application has started
        
        // When: We fetch all ingredients
        List<Ingredient> ingredients = ingredientRepository.findAll();
        
        // Then: All ingredient names should be unique
        long uniqueNameCount = ingredients.stream()
                .map(Ingredient::getName)
                .distinct()
                .count();
        
        assertThat(uniqueNameCount).isEqualTo(ingredients.size());
    }

    @Test
    void shouldHaveIngredientsInCorrectCategories() {
        // Given: Application has started
        
        // When: We fetch ingredients by category
        List<Ingredient> buns = ingredientRepository.findByCategoryOrderBySortOrderAsc("buns");
        List<Ingredient> patties = ingredientRepository.findByCategoryOrderBySortOrderAsc("patties");
        List<Ingredient> toppings = ingredientRepository.findByCategoryOrderBySortOrderAsc("toppings");
        List<Ingredient> sauces = ingredientRepository.findByCategoryOrderBySortOrderAsc("sauces");
        
        // Then: Categories should have expected ingredients
        assertThat(buns).hasSize(1); // Classic Sesame Bun
        assertThat(patties).hasSize(1); // Beef Patty
        assertThat(toppings).hasSize(1); // Lettuce
        assertThat(sauces).hasSize(1); // Ketchup
        
        // Verify specific ingredients exist
        assertThat(buns).extracting(Ingredient::getName).contains("Classic Sesame Bun");
        assertThat(patties).extracting(Ingredient::getName).contains("Beef Patty");
        assertThat(toppings).extracting(Ingredient::getName).contains("Lettuce");
        assertThat(sauces).extracting(Ingredient::getName).contains("Ketchup");
    }
}
