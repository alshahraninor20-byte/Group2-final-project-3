package com.burgerbuilder.controller;

import com.burgerbuilder.dto.IngredientDto;
import com.burgerbuilder.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
@Slf4j
public class IngredientController {
    
    private final IngredientService ingredientService;
    
    @GetMapping
    public ResponseEntity<List<IngredientDto>> getAllIngredients() {
        log.debug("GET /api/ingredients - Fetching all ingredients");
        List<IngredientDto> ingredients = ingredientService.getAllIngredients();
        return ResponseEntity.ok(ingredients);
    }
    
    @GetMapping("/{category}")
    public ResponseEntity<List<IngredientDto>> getIngredientsByCategory(
            @PathVariable String category) {
        log.debug("GET /api/ingredients/{} - Fetching ingredients by category", category);
        List<IngredientDto> ingredients = ingredientService.getIngredientsByCategory(category);
        return ResponseEntity.ok(ingredients);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        log.debug("GET /api/ingredients/categories - Fetching all categories");
        List<String> categories = ingredientService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/id/{id}")
    public ResponseEntity<IngredientDto> getIngredientById(@PathVariable Long id) {
        log.debug("GET /api/ingredients/id/{} - Fetching ingredient by id", id);
        IngredientDto ingredient = ingredientService.getIngredientById(id);
        return ResponseEntity.ok(ingredient);
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<IngredientDto> getIngredientByName(@PathVariable String name) {
        log.debug("GET /api/ingredients/name/{} - Fetching ingredient by name", name);
        IngredientDto ingredient = ingredientService.getIngredientByName(name);
        return ResponseEntity.ok(ingredient);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<IngredientDto>> searchIngredients(
            @RequestParam String searchTerm) {
        log.debug("GET /api/ingredients/search?searchTerm={} - Searching ingredients", searchTerm);
        List<IngredientDto> ingredients = ingredientService.searchIngredients(searchTerm);
        return ResponseEntity.ok(ingredients);
    }
    
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }
}
