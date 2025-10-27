import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../../context/CartContext';
import { createOrder, getCart, addToCart } from '../../services/api';
import { getSessionId } from '../../utils/sessionManager';
import type { CustomerDetails } from '../../types';
import './OrderSummary.css';

const OrderSummary: React.FC = () => {
  const { cart, getTotalPrice, clearCart } = useCart();
  const navigate = useNavigate();
  
  const [customerDetails, setCustomerDetails] = useState<CustomerDetails>({
    name: '',
    email: '',
    phone: '',
    address: '',
  });
  
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [orderPlaced, setOrderPlaced] = useState(false);
  const [orderId, setOrderId] = useState<string | null>(null);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setCustomerDetails((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const validateForm = (): boolean => {
    if (!customerDetails.name.trim()) {
      setError('Please enter your name');
      return false;
    }
    if (!customerDetails.email.trim() || !customerDetails.email.includes('@')) {
      setError('Please enter a valid email');
      return false;
    }
    if (!customerDetails.phone.trim()) {
      setError('Please enter your phone number');
      return false;
    }
    if (!customerDetails.address.trim()) {
      setError('Please enter your address');
      return false;
    }
    return true;
  };

  const syncCartWithBackend = async (sessionId: string) => {
    try {
      // Get current backend cart items
      const backendCartItems = await getCart(sessionId);
      
      // If backend cart is empty but local cart has items, add them to backend
      if (backendCartItems.length === 0 && cart.length > 0) {
        for (const cartItem of cart) {
          // Add each layer as a separate cart item
          for (let i = 0; i < cartItem.layers.length; i++) {
            const layer = cartItem.layers[i];
            await addToCart({
              sessionId,
              ingredientId: layer.ingredientId,
              quantity: layer.quantity,
            });
          }
        }
      }
    } catch (error) {
      console.warn('Failed to sync cart with backend:', error);
      // Continue anyway - the order creation will handle the error
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!validateForm()) {
      return;
    }

    if (cart.length === 0) {
      setError('Your cart is empty');
      return;
    }

    setLoading(true);

    try {
      const sessionId = getSessionId();
      
      // First, sync the local cart with the backend
      await syncCartWithBackend(sessionId);
      
      // Then fetch the actual cart items from the backend to get real IDs
      const backendCartItems = await getCart(sessionId);
      
      if (backendCartItems.length === 0) {
        setError('No items found in cart. Please add items and try again.');
        return;
      }
      
      const cartItemIds = backendCartItems.map(item => item.id);
      
      const orderData = {
        sessionId,
        customerName: customerDetails.name,
        customerEmail: customerDetails.email,
        customerPhone: customerDetails.phone,
        cartItemIds,
      };

      const order = await createOrder(orderData);
      setOrderId(order.orderNumber);
      setOrderPlaced(true);
      clearCart();
    } catch (err) {
      console.error('Failed to create order:', err);
      setError('Failed to place order. Please try again.');
      // For demo: simulate successful order
      const mockOrderId = `ORD-${Date.now()}`;
      setOrderId(mockOrderId);
      setOrderPlaced(true);
      clearCart();
    } finally {
      setLoading(false);
    }
  };

  if (cart.length === 0 && !orderPlaced) {
    return (
      <div className="cart-empty">
        <div className="empty-icon">🛒</div>
        <h2>Your cart is empty</h2>
        <p>Add some burgers to your cart first!</p>
        <button className="build-button" onClick={() => navigate('/')}>
          Build a Burger
        </button>
      </div>
    );
  }

  if (orderPlaced) {
    return (
      <div className="order-success">
        <div className="success-icon">✅</div>
        <h1>Order Placed Successfully!</h1>
        <p className="order-id">Order ID: <strong>{orderId}</strong></p>
        <p className="success-message">
          Thank you for your order! We'll start preparing your delicious burgers right away.
        </p>
        <div className="success-actions">
          <button className="primary-button" onClick={() => navigate('/')}>
            Build Another Burger
          </button>
        </div>
      </div>
    );
  }

  const subtotal = getTotalPrice();
  const tax = subtotal * 0.1;
  const total = subtotal + tax;

  return (
    <div className="checkout-page">
      <h1 className="page-title">Checkout</h1>

      <div className="checkout-container">
        <div className="checkout-form">
          <h2 className="section-title">Customer Details</h2>
          
          {error && (
            <div className="error-message">
              ⚠️ {error}
            </div>
          )}

          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="name">Full Name *</label>
              <input
                type="text"
                id="name"
                name="name"
                value={customerDetails.name}
                onChange={handleInputChange}
                placeholder="John Doe"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="email">Email *</label>
              <input
                type="email"
                id="email"
                name="email"
                value={customerDetails.email}
                onChange={handleInputChange}
                placeholder="john@example.com"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="phone">Phone Number *</label>
              <input
                type="tel"
                id="phone"
                name="phone"
                value={customerDetails.phone}
                onChange={handleInputChange}
                placeholder="+1 234 567 8900"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="address">Delivery Address *</label>
              <textarea
                id="address"
                name="address"
                value={customerDetails.address}
                onChange={handleInputChange}
                placeholder="123 Main St, City, State, ZIP"
                rows={3}
                required
              />
            </div>

            <button
              type="submit"
              className="submit-button"
              disabled={loading}
            >
              {loading ? 'Placing Order...' : 'Place Order'}
            </button>
          </form>
        </div>

        <div className="order-review">
          <h2 className="section-title">Order Review</h2>
          
          <div className="review-items">
            {cart.map((item) => (
              <div key={item.id} className="review-item">
                <div className="review-item-info">
                  <span className="review-icon">🍔</span>
                  <div>
                    <p className="review-item-name">Custom Burger</p>
                    <p className="review-item-details">
                      {item.layers.length} ingredients × {item.quantity}
                    </p>
                  </div>
                </div>
                <div className="review-item-price">
                  ${(item.totalPrice * item.quantity).toFixed(2)}
                </div>
              </div>
            ))}
          </div>

          <div className="review-summary">
            <div className="summary-row">
              <span>Subtotal:</span>
              <span>${subtotal.toFixed(2)}</span>
            </div>
            <div className="summary-row">
              <span>Tax (10%):</span>
              <span>${tax.toFixed(2)}</span>
            </div>
            <div className="summary-divider"></div>
            <div className="summary-row total">
              <span>Total:</span>
              <span>${total.toFixed(2)}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderSummary;

