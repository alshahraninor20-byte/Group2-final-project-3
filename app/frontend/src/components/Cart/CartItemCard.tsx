import React from 'react';
import type { CartItem } from '../../types';
import './CartItemCard.css';

interface CartItemCardProps {
  item: CartItem;
  onUpdateQuantity: (itemId: number, quantity: number) => void;
  onRemove: (itemId: number) => void;
}

const CartItemCard: React.FC<CartItemCardProps> = ({ item, onUpdateQuantity, onRemove }) => {
  return (
    <div className="cart-item-card">
      <div className="item-info">
        <div className="item-icon">🍔</div>
        <div className="item-details">
          <h3 className="item-title">Custom Burger</h3>
          <p className="item-layers">{item.layers.length} ingredients</p>
          <p className="item-price">${item.totalPrice.toFixed(2)} each</p>
        </div>
      </div>

      <div className="item-actions">
        <div className="quantity-controls">
          <button
            className="quantity-button"
            onClick={() => onUpdateQuantity(item.id, item.quantity - 1)}
            aria-label="Decrease quantity"
          >
            −
          </button>
          <span className="quantity-value">{item.quantity}</span>
          <button
            className="quantity-button"
            onClick={() => onUpdateQuantity(item.id, item.quantity + 1)}
            aria-label="Increase quantity"
          >
            +
          </button>
        </div>

        <div className="item-total">
          <span className="total-label">Total:</span>
          <span className="total-value">${(item.totalPrice * item.quantity).toFixed(2)}</span>
        </div>

        <button
          className="remove-button"
          onClick={() => onRemove(item.id)}
          aria-label="Remove item"
        >
          🗑️ Remove
        </button>
      </div>
    </div>
  );
};

export default CartItemCard;

