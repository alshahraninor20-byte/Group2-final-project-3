package com.burgerbuilder.service;

import com.burgerbuilder.dto.IngredientDto;
import com.burgerbuilder.entity.Ingredient;
import com.burgerbuilder.exception.ResourceNotFoundException;
import com.burgerbuilder.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class IngredientService {
    
    private final IngredientRepository ingredientRepository;
    
    public List<IngredientDto> getAllIngredients() {
        log.debug("Fetching all available ingredients");
        List<Ingredient> ingredients = ingredientRepository.findByIsAvailableTrueOrderBySortOrderAsc();
        return ingredients.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<IngredientDto> getIngredientsByCategory(String category) {
        log.debug("Fetching ingredients for category: {}", category);
        List<Ingredient> ingredients = ingredientRepository.findByCategoryAndIsAvailableTrueOrderBySortOrderAsc(category);
        return ingredients.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<String> getAllCategories() {
        log.debug("Fetching all ingredient categories");
        return ingredientRepository.findDistinctCategories();
    }
    
    public IngredientDto getIngredientById(Long id) {
        log.debug("Fetching ingredient with id: {}", id);
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "id", id));
        
        if (!ingredient.getIsAvailable()) {
            throw new ResourceNotFoundException("Ingredient", "id", id);
        }
        
        return convertToDto(ingredient);
    }
    
    public IngredientDto getIngredientByName(String name) {
        log.debug("Fetching ingredient with name: {}", name);
        Ingredient ingredient = ingredientRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "name", name));
        
        if (!ingredient.getIsAvailable()) {
            throw new ResourceNotFoundException("Ingredient", "name", name);
        }
        
        return convertToDto(ingredient);
    }
    
    public List<IngredientDto> searchIngredients(String searchTerm) {
        log.debug("Searching ingredients with term: {}", searchTerm);
        List<Ingredient> ingredients = ingredientRepository.searchIngredients(searchTerm);
        return ingredients.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public IngredientDto convertToDto(Ingredient ingredient) {
        IngredientDto dto = new IngredientDto();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setCategory(ingredient.getCategory());
        dto.setPrice(ingredient.getPrice());
        dto.setDescription(ingredient.getDescription());
        dto.setImageUrl(ingredient.getImageUrl());
        dto.setIsAvailable(ingredient.getIsAvailable());
        dto.setSortOrder(ingredient.getSortOrder());
        return dto;
    }
}
