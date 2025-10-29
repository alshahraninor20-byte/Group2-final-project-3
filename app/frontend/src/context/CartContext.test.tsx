import { describe, it, expect, beforeEach } from 'vitest';
import { renderHook, act } from '@testing-library/react';
import { CartProvider, useCart } from './CartContext';
import type { CartItem } from '../types';

const wrapper = ({ children }: { children: React.ReactNode }) => (
  <CartProvider>{children}</CartProvider>
);

describe('CartContext', () => {
  beforeEach(() => {
    localStorage.clear();
  });

  const mockCartItem: CartItem = {
    id: 1,
    layers: [{ ingredientId: 1, quantity: 2 }],
    totalPrice: 10.99,
    quantity: 1,
  };

  it('should initialize with empty cart', () => {
    const { result } = renderHook(() => useCart(), { wrapper });
    expect(result.current.cart).toEqual([]);
  });

  it('should add item to cart', () => {
    const { result } = renderHook(() => useCart(), { wrapper });
    
    act(() => {
      result.current.addItemToCart(mockCartItem);
    });

    expect(result.current.cart).toHaveLength(1);
    expect(result.current.cart[0]).toEqual(mockCartItem);
  });

  it('should remove item from cart', () => {
    const { result } = renderHook(() => useCart(), { wrapper });
    
    act(() => {
      result.current.addItemToCart(mockCartItem);
    });

    act(() => {
      result.current.removeItemFromCart(mockCartItem.id);
    });

    expect(result.current.cart).toHaveLength(0);
  });

  it('should update item quantity', () => {
    const { result } = renderHook(() => useCart(), { wrapper });
    
    act(() => {
      result.current.addItemToCart(mockCartItem);
    });

    act(() => {
      result.current.updateItemQuantity(mockCartItem.id, 3);
    });

    expect(result.current.cart[0].quantity).toBe(3);
  });

  it('should remove item when quantity updated to 0', () => {
    const { result } = renderHook(() => useCart(), { wrapper });
    
    act(() => {
      result.current.addItemToCart(mockCartItem);
    });

    act(() => {
      result.current.updateItemQuantity(mockCartItem.id, 0);
    });

    expect(result.current.cart).toHaveLength(0);
  });

  it('should clear entire cart', () => {
    const { result } = renderHook(() => useCart(), { wrapper });
    
    act(() => {
      result.current.addItemToCart(mockCartItem);
      result.current.addItemToCart({ ...mockCartItem, id: 2 });
    });

    act(() => {
      result.current.clearCart();
    });

    expect(result.current.cart).toHaveLength(0);
  });

  it('should calculate total price correctly', () => {
    const { result } = renderHook(() => useCart(), { wrapper });
    
    act(() => {
      result.current.addItemToCart({ ...mockCartItem, totalPrice: 10, quantity: 2 });
      result.current.addItemToCart({ ...mockCartItem, id: 2, totalPrice: 5, quantity: 1 });
    });

    expect(result.current.getTotalPrice()).toBe(25);
  });

  it('should calculate total items correctly', () => {
    const { result } = renderHook(() => useCart(), { wrapper });
    
    act(() => {
      result.current.addItemToCart({ ...mockCartItem, quantity: 2 });
      result.current.addItemToCart({ ...mockCartItem, id: 2, quantity: 3 });
    });

    expect(result.current.getTotalItems()).toBe(5);
  });

  it('should persist cart to localStorage', () => {
    const { result } = renderHook(() => useCart(), { wrapper });
    
    act(() => {
      result.current.addItemToCart(mockCartItem);
    });

    const storedCart = localStorage.getItem('cart');
    expect(storedCart).toBeTruthy();
    expect(JSON.parse(storedCart!)).toHaveLength(1);
  });

  it('should load cart from localStorage on mount', () => {
    const initialCart = [mockCartItem];
    localStorage.setItem('cart', JSON.stringify(initialCart));

    const { result } = renderHook(() => useCart(), { wrapper });

    expect(result.current.cart).toHaveLength(1);
    expect(result.current.cart[0]).toEqual(mockCartItem);
  });

  it('should throw error when used outside provider', () => {
    expect(() => {
      renderHook(() => useCart());
    }).toThrow('useCart must be used within a CartProvider');
  });
});

