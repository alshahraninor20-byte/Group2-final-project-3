import React from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../../context/CartContext';
import './Header.css';

const Header: React.FC = () => {
  const { getTotalItems } = useCart();
  const itemCount = getTotalItems();

  return (
    <header className="header">
      <div className="header-container">
        <Link to="/" className="logo">
          <h1>🍔 Burger Builder</h1>
        </Link>
        
        <nav className="nav">
          <Link to="/" className="nav-link">Build</Link>
          <Link to="/orders" className="nav-link">Orders</Link>
          <Link to="/cart" className="nav-link cart-link">
            <span className="cart-icon">🛒</span>
            Cart
            {itemCount > 0 && (
              <span className="cart-badge">{itemCount}</span>
            )}
          </Link>
        </nav>
      </div>
    </header>
  );
};

export default Header;

