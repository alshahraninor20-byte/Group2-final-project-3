import React from 'react';
import type { Ingredient } from '../../types';
import './IngredientCard.css';

interface IngredientCardProps {
  ingredient: Ingredient;
  onAdd: (ingredientId: number) => void;
}

const IngredientCard: React.FC<IngredientCardProps> = ({ ingredient, onAdd }) => {
  return (
    <div className="ingredient-card" onClick={() => onAdd(ingredient.id)}>
      <div className="ingredient-icon">{getCategoryIcon(ingredient.category)}</div>
      <h3 className="ingredient-name">{ingredient.name}</h3>
      <p className="ingredient-price">${ingredient.price.toFixed(2)}</p>
      <button className="add-button">Add +</button>
    </div>
  );
};

const getCategoryIcon = (category: string): string => {
  const icons: Record<string, string> = {
    buns: '🍞',
    patties: '🥩',
    toppings: '🥬',
    sauces: '🧂',
  };
  return icons[category] || '🍔';
};

export default IngredientCard;

