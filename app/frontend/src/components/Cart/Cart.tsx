import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../../context/CartContext';
import CartItemCard from './CartItemCard';
import './Cart.css';

const Cart: React.FC = () => {
  const { cart, removeItemFromCart, updateItemQuantity, clearCart, getTotalPrice } = useCart();
  const navigate = useNavigate();

  const handleCheckout = () => {
    if (cart.length === 0) return;
    navigate('/checkout');
  };

  if (cart.length === 0) {
    return (
      <div className="cart-empty">
        <div className="empty-icon">🛒</div>
        <h2>Your cart is empty</h2>
        <p>Start building your perfect burger!</p>
        <button className="build-button" onClick={() => navigate('/')}>
          Build a Burger
        </button>
      </div>
    );
  }

  return (
    <div className="cart-page">
      <div className="cart-header">
        <h1>Your Cart</h1>
        <button className="clear-all-button" onClick={clearCart}>
          Clear All
        </button>
      </div>

      <div className="cart-content">
        <div className="cart-items">
          {cart.map((item) => (
            <CartItemCard
              key={item.id}
              item={item}
              onUpdateQuantity={updateItemQuantity}
              onRemove={removeItemFromCart}
            />
          ))}
        </div>

        <div className="cart-summary">
          <div className="summary-card">
            <h2 className="summary-title">Order Summary</h2>
            
            <div className="summary-row">
              <span className="summary-label">Items:</span>
              <span className="summary-value">{cart.length}</span>
            </div>

            <div className="summary-row">
              <span className="summary-label">Subtotal:</span>
              <span className="summary-value">${getTotalPrice().toFixed(2)}</span>
            </div>

            <div className="summary-row">
              <span className="summary-label">Tax (10%):</span>
              <span className="summary-value">${(getTotalPrice() * 0.1).toFixed(2)}</span>
            </div>

            <div className="summary-divider"></div>

            <div className="summary-row total-row">
              <span className="summary-label">Total:</span>
              <span className="summary-value total">${(getTotalPrice() * 1.1).toFixed(2)}</span>
            </div>

            <button className="checkout-button" onClick={handleCheckout}>
              Proceed to Checkout
            </button>

            <button className="continue-shopping" onClick={() => navigate('/')}>
              Continue Shopping
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Cart;

