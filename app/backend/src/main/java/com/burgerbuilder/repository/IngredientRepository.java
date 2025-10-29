package com.burgerbuilder.repository;

import com.burgerbuilder.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    
    List<Ingredient> findByCategoryOrderBySortOrderAsc(String category);
    
    List<Ingredient> findByIsAvailableTrueOrderBySortOrderAsc();
    
    List<Ingredient> findByCategoryAndIsAvailableTrueOrderBySortOrderAsc(String category);
    
    Optional<Ingredient> findByName(String name);
    
    @Query("SELECT DISTINCT i.category FROM Ingredient i WHERE i.isAvailable = true ORDER BY i.category")
    List<String> findDistinctCategories();
    
    @Query("SELECT i FROM Ingredient i WHERE i.isAvailable = true AND " +
           "LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Ingredient> searchIngredients(@Param("searchTerm") String searchTerm);
}
