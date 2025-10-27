import React, { useEffect, useState } from 'react';
import { useBurgerBuilder } from '../../context/BurgerBuilderContext';
import { useCart } from '../../context/CartContext';
import { getIngredients } from '../../services/api';
import BurgerPreview from './BurgerPreview';
import IngredientList from '../Ingredients/IngredientList';
import './BurgerBuilder.css';

const BurgerBuilder: React.FC = () => {
  const {
    layers,
    ingredients,
    setIngredients,
    addLayer,
    removeLayer,
    clearLayers,
    getTotalPrice,
    getIngredientById,
  } = useBurgerBuilder();

  const { addItemToCart } = useCart();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [notification, setNotification] = useState<string | null>(null);

  useEffect(() => {
    loadIngredients();
  }, []);

  const loadIngredients = async () => {
    try {
      setLoading(true);
      const data = await getIngredients();
      // Handle both grouped format (legacy) and flat list format (current backend)
      const allIngredients = Array.isArray(data) 
        ? data 
        : [
            ...data.buns,
            ...data.patties,
            ...data.toppings,
            ...data.sauces,
          ];
      setIngredients(allIngredients);
      setError(null);
    } catch (err) {
      setError('Failed to load ingredients. Using sample data for demo.');
      // Set sample data for demo purposes
      setSampleIngredients();
    } finally {
      setLoading(false);
    }
  };

  const setSampleIngredients = () => {
    const sampleIngredients = [
      { id: 1, name: 'Sesame Bun', category: 'buns' as const, price: 1.5, imageUrl: '' },
      { id: 2, name: 'Whole Wheat Bun', category: 'buns' as const, price: 1.8, imageUrl: '' },
      { id: 3, name: 'Beef Patty', category: 'patties' as const, price: 3.5, imageUrl: '' },
      { id: 4, name: 'Chicken Patty', category: 'patties' as const, price: 3.0, imageUrl: '' },
      { id: 5, name: 'Veggie Patty', category: 'patties' as const, price: 2.8, imageUrl: '' },
      { id: 6, name: 'Lettuce', category: 'toppings' as const, price: 0.5, imageUrl: '' },
      { id: 7, name: 'Tomato', category: 'toppings' as const, price: 0.5, imageUrl: '' },
      { id: 8, name: 'Onion', category: 'toppings' as const, price: 0.3, imageUrl: '' },
      { id: 9, name: 'Cheese', category: 'toppings' as const, price: 1.0, imageUrl: '' },
      { id: 10, name: 'Pickles', category: 'toppings' as const, price: 0.4, imageUrl: '' },
      { id: 11, name: 'Ketchup', category: 'sauces' as const, price: 0.2, imageUrl: '' },
      { id: 12, name: 'Mayo', category: 'sauces' as const, price: 0.2, imageUrl: '' },
      { id: 13, name: 'Mustard', category: 'sauces' as const, price: 0.2, imageUrl: '' },
      { id: 14, name: 'BBQ Sauce', category: 'sauces' as const, price: 0.3, imageUrl: '' },
    ];
    setIngredients(sampleIngredients);
  };

  const handleAddToCart = () => {
    if (layers.length === 0) {
      showNotification('Please add some ingredients first!');
      return;
    }

    const cartItem = {
      id: Date.now(),
      layers: layers,
      totalPrice: getTotalPrice(),
      quantity: 1,
    };

    addItemToCart(cartItem);
    clearLayers();
    showNotification('Burger added to cart! 🎉');
  };

  const showNotification = (message: string) => {
    setNotification(message);
    setTimeout(() => setNotification(null), 3000);
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner">🍔</div>
        <p>Loading ingredients...</p>
      </div>
    );
  }

  return (
    <div className="burger-builder">
      {notification && (
        <div className="notification">{notification}</div>
      )}
      
      {error && (
        <div className="error-banner">
          ⚠️ {error}
        </div>
      )}

      <div className="builder-container">
        <div className="builder-left">
          <div className="builder-header">
            <h1>Build Your Burger</h1>
            <p>Select ingredients to create your perfect burger</p>
          </div>
          <IngredientList
            ingredients={ingredients}
            onAddIngredient={addLayer}
          />
        </div>

        <div className="builder-right">
          <BurgerPreview
            layers={layers}
            getIngredientById={getIngredientById}
            onRemoveLayer={removeLayer}
          />
          
          <div className="builder-actions">
            <div className="price-display">
              <span className="price-label">Total:</span>
              <span className="price-value">${getTotalPrice().toFixed(2)}</span>
            </div>
            
            <div className="action-buttons">
              <button
                className="clear-button"
                onClick={clearLayers}
                disabled={layers.length === 0}
              >
                Clear
              </button>
              <button
                className="add-to-cart-button"
                onClick={handleAddToCart}
                disabled={layers.length === 0}
              >
                Add to Cart
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BurgerBuilder;

