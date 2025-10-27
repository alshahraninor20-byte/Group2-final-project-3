import React from 'react';
import type { BurgerLayer, Ingredient } from '../../types';
import './BurgerPreview.css';

interface BurgerPreviewProps {
  layers: BurgerLayer[];
  getIngredientById: (id: number) => Ingredient | undefined;
  onRemoveLayer: (index: number) => void;
}

const BurgerPreview: React.FC<BurgerPreviewProps> = ({ layers, getIngredientById, onRemoveLayer }) => {
  const getIngredientDisplay = (ingredient: Ingredient | undefined) => {
    if (!ingredient) return { icon: '❓', className: 'unknown' };
    
    const categoryDisplays: Record<string, { icon: string; className: string }> = {
      buns: { icon: '🍞', className: 'bun' },
      patties: { icon: '🥩', className: 'patty' },
      toppings: { icon: '🥬', className: 'topping' },
      sauces: { icon: '🧂', className: 'sauce' },
    };
    
    return categoryDisplays[ingredient.category] || { icon: '🍔', className: 'other' };
  };

  return (
    <div className="burger-preview">
      <h2 className="preview-title">Your Burger</h2>
      <div className="burger-stack">
        <div className="burger-top-bun">🍔 Top Bun</div>
        {layers.length === 0 ? (
          <div className="empty-burger">
            <p>Start building your burger!</p>
            <p className="hint">Click ingredients to add them</p>
          </div>
        ) : (
          layers.map((layer, index) => {
            const ingredient = getIngredientById(layer.ingredientId);
            const display = getIngredientDisplay(ingredient);
            
            return (
              <div
                key={index}
                className={`burger-layer ${display.className}`}
                onClick={() => onRemoveLayer(index)}
                title="Click to remove"
              >
                <span className="layer-icon">{display.icon}</span>
                <span className="layer-name">{ingredient?.name || 'Unknown'}</span>
                {layer.quantity > 1 && (
                  <span className="layer-quantity">x{layer.quantity}</span>
                )}
              </div>
            );
          })
        )}
        <div className="burger-bottom-bun">🍔 Bottom Bun</div>
      </div>
    </div>
  );
};

export default BurgerPreview;

