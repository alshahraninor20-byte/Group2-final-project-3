import React, { useState, useEffect } from 'react';
import { getOrderHistory, getOrdersByCustomerEmail } from '../../services/api';
import type { Order } from '../../types';
import './OrderHistory.css';

const OrderHistory: React.FC = () => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filterEmail, setFilterEmail] = useState('');
  const [showFilter, setShowFilter] = useState(false);

  useEffect(() => {
    loadOrderHistory();
  }, []);

  const loadOrderHistory = async (email?: string) => {
    try {
      setLoading(true);
      setError(null);
      
      let orderData: Order[];
      if (email && email.trim()) {
        orderData = await getOrdersByCustomerEmail(email.trim());
      } else {
        orderData = await getOrderHistory();
      }
      
      setOrders(orderData);
    } catch (err) {
      console.error('Failed to load order history:', err);
      setError('Failed to load order history. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleFilterSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    loadOrderHistory(filterEmail);
  };

  const clearFilter = () => {
    setFilterEmail('');
    setShowFilter(false);
    loadOrderHistory();
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const getStatusColor = (status: string) => {
    switch (status.toLowerCase()) {
      case 'pending':
        return '#ffc107';
      case 'confirmed':
        return '#17a2b8';
      case 'preparing':
        return '#fd7e14';
      case 'ready':
        return '#28a745';
      case 'delivered':
        return '#6f42c1';
      case 'cancelled':
        return '#dc3545';
      default:
        return '#6c757d';
    }
  };

  const getStatusIcon = (status: string) => {
    switch (status.toLowerCase()) {
      case 'pending':
        return '⏳';
      case 'confirmed':
        return '✅';
      case 'preparing':
        return '🍳';
      case 'ready':
        return '📦';
      case 'delivered':
        return '🚚';
      case 'cancelled':
        return '❌';
      default:
        return '❓';
    }
  };

  if (loading) {
    return (
      <div className="order-history">
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <p>Loading order history...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="order-history">
        <div className="error-container">
          <div className="error-icon">⚠️</div>
          <p>{error}</p>
          <button className="retry-button" onClick={() => loadOrderHistory()}>
            Try Again
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="order-history">
      <div className="order-history-header">
        <h1>Order History</h1>
        <div className="header-actions">
          <button 
            className="filter-button"
            onClick={() => setShowFilter(!showFilter)}
          >
            🔍 Filter by Email
          </button>
          <button 
            className="refresh-button"
            onClick={() => loadOrderHistory(filterEmail)}
          >
            🔄 Refresh
          </button>
        </div>
      </div>

      {showFilter && (
        <div className="filter-section">
          <form onSubmit={handleFilterSubmit}>
            <div className="filter-input-group">
              <input
                type="email"
                placeholder="Enter customer email to filter"
                value={filterEmail}
                onChange={(e) => setFilterEmail(e.target.value)}
                className="filter-input"
              />
              <button type="submit" className="filter-submit-button">
                Filter
              </button>
              <button 
                type="button" 
                onClick={clearFilter}
                className="clear-filter-button"
              >
                Clear
              </button>
            </div>
          </form>
        </div>
      )}

      {orders.length === 0 ? (
        <div className="no-orders">
          <div className="no-orders-icon">📋</div>
          <h2>No orders found</h2>
          <p>
            {filterEmail 
              ? `No orders found for email: ${filterEmail}`
              : 'No orders have been placed yet.'
            }
          </p>
          {filterEmail && (
            <button className="clear-filter-button" onClick={clearFilter}>
              Show All Orders
            </button>
          )}
        </div>
      ) : (
        <div className="orders-list">
          {orders.map((order) => (
            <div key={order.id} className="order-card">
              <div className="order-header">
                <div className="order-info">
                  <h3 className="order-number">#{order.orderNumber}</h3>
                  <p className="order-date">{formatDate(order.createdAt)}</p>
                </div>
                <div 
                  className="order-status"
                  style={{ backgroundColor: getStatusColor(order.status) }}
                >
                  <span className="status-icon">{getStatusIcon(order.status)}</span>
                  <span className="status-text">{order.status}</span>
                </div>
              </div>
              
              <div className="order-details">
                <div className="customer-info">
                  <p><strong>Customer:</strong> {order.customerName}</p>
                  <p><strong>Email:</strong> {order.customerEmail}</p>
                  {order.customerPhone && (
                    <p><strong>Phone:</strong> {order.customerPhone}</p>
                  )}
                </div>
                
                <div className="order-summary">
                  <p className="total-amount">
                    <strong>Total: ${order.totalAmount.toFixed(2)}</strong>
                  </p>
                  {order.orderItems && order.orderItems.length > 0 && (
                    <div className="order-items-count">
                      {order.orderItems.length} item(s)
                    </div>
                  )}
                </div>
              </div>

              {order.updatedAt && order.updatedAt !== order.createdAt && (
                <div className="order-updated">
                  <small>Last updated: {formatDate(order.updatedAt)}</small>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default OrderHistory;
