import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { CartProvider } from './context/CartContext';
import { BurgerBuilderProvider } from './context/BurgerBuilderContext';
import Layout from './components/Layout/Layout';
import BurgerBuilder from './components/BurgerBuilder/BurgerBuilder';
import Cart from './components/Cart/Cart';
import OrderSummary from './components/OrderSummary/OrderSummary';
import OrderHistory from './components/OrderHistory/OrderHistory';
import './App.css';

const App: React.FC = () => {
  return (
    <Router>
      <CartProvider>
        <BurgerBuilderProvider>
          <Layout>
            <Routes>
              <Route path="/" element={<BurgerBuilder />} />
              <Route path="/cart" element={<Cart />} />
              <Route path="/checkout" element={<OrderSummary />} />
              <Route path="/orders" element={<OrderHistory />} />
            </Routes>
          </Layout>
        </BurgerBuilderProvider>
      </CartProvider>
    </Router>
  );
};

export default App;
