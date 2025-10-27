import React from 'react';
import type { Ingredient, IngredientCategory } from '../../types';
import IngredientCard from './IngredientCard';
import './IngredientList.css';

interface IngredientListProps {
  ingredients: Ingredient[];
  onAddIngredient: (ingredientId: number) => void;
}

const IngredientList: React.FC<IngredientListProps> = ({ ingredients, onAddIngredient }) => {
  const categorizedIngredients = ingredients.reduce((acc, ingredient) => {
    if (!acc[ingredient.category]) {
      acc[ingredient.category] = [];
    }
    acc[ingredient.category].push(ingredient);
    return acc;
  }, {} as Record<IngredientCategory, Ingredient[]>);

  const categoryOrder: IngredientCategory[] = ['buns', 'patties', 'toppings', 'sauces'];

  return (
    <div className="ingredient-list">
      {categoryOrder.map((category) => {
        const items = categorizedIngredients[category];
        if (!items || items.length === 0) return null;

        return (
          <div key={category} className="ingredient-category">
            <h2 className="category-title">
              {category.charAt(0).toUpperCase() + category.slice(1)}
            </h2>
            <div className="ingredient-grid">
              {items.map((ingredient) => (
                <IngredientCard
                  key={ingredient.id}
                  ingredient={ingredient}
                  onAdd={onAddIngredient}
                />
              ))}
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default IngredientList;

