import { describe, it, expect } from 'vitest';
import { renderHook, act } from '@testing-library/react';
import { BurgerBuilderProvider, useBurgerBuilder } from './BurgerBuilderContext';
import type { Ingredient } from '../types';

const wrapper = ({ children }: { children: React.ReactNode }) => (
  <BurgerBuilderProvider>{children}</BurgerBuilderProvider>
);

describe('BurgerBuilderContext', () => {
  const mockIngredients: Ingredient[] = [
    {
      id: 1,
      name: 'Sesame Bun',
      category: 'buns',
      price: 2.0,
      imageUrl: 'bun.jpg',
    },
    {
      id: 2,
      name: 'Beef Patty',
      category: 'patties',
      price: 5.0,
      imageUrl: 'patty.jpg',
    },
    {
      id: 3,
      name: 'Lettuce',
      category: 'toppings',
      price: 0.5,
      imageUrl: 'lettuce.jpg',
    },
  ];

  it('should initialize with empty layers', () => {
    const { result } = renderHook(() => useBurgerBuilder(), { wrapper });
    expect(result.current.layers).toEqual([]);
  });

  it('should set ingredients', () => {
    const { result } = renderHook(() => useBurgerBuilder(), { wrapper });
    
    act(() => {
      result.current.setIngredients(mockIngredients);
    });

    expect(result.current.ingredients).toEqual(mockIngredients);
  });

  it('should add a new layer', () => {
    const { result } = renderHook(() => useBurgerBuilder(), { wrapper });
    
    act(() => {
      result.current.addLayer(1);
    });

    expect(result.current.layers).toHaveLength(1);
    expect(result.current.layers[0]).toEqual({ ingredientId: 1, quantity: 1 });
  });

  it('should increment quantity when adding existing layer', () => {
    const { result } = renderHook(() => useBurgerBuilder(), { wrapper });
    
    act(() => {
      result.current.addLayer(1);
      result.current.addLayer(1);
    });

    expect(result.current.layers).toHaveLength(1);
    expect(result.current.layers[0].quantity).toBe(2);
  });

  it('should add different ingredients as separate layers', () => {
    const { result } = renderHook(() => useBurgerBuilder(), { wrapper });
    
    act(() => {
      result.current.addLayer(1);
      result.current.addLayer(2);
    });

    expect(result.current.layers).toHaveLength(2);
  });

  it('should decrement quantity when removing layer', () => {
    const { result } = renderHook(() => useBurgerBuilder(), { wrapper });
    
    act(() => {
      result.current.addLayer(1);
      result.current.addLayer(1);
    });

    act(() => {
      result.current.removeLayer(0);
    });

    expect(result.current.layers[0].quantity).toBe(1);
  });

  it('should remove layer completely when quantity reaches 0', () => {
    const { result } = renderHook(() => useBurgerBuilder(), { wrapper });
    
    act(() => {
      result.current.addLayer(1);
    });

    act(() => {
      result.current.removeLayer(0);
    });

    expect(result.current.layers).toHaveLength(0);
  });

  it('should clear all layers', () => {
    const { result } = renderHook(() => useBurgerBuilder(), { wrapper });
    
    act(() => {
      result.current.addLayer(1);
      result.current.addLayer(2);
    });

    act(() => {
      result.current.clearLayers();
    });

    expect(result.current.layers).toHaveLength(0);
  });

  it('should get ingredient by id', () => {
    const { result } = renderHook(() => useBurgerBuilder(), { wrapper });
    
    act(() => {
      result.current.setIngredients(mockIngredients);
    });

    const ingredient = result.current.getIngredientById(2);
    expect(ingredient).toEqual(mockIngredients[1]);
  });

  it('should return undefined for non-existent ingredient', () => {
    const { result } = renderHook(() => useBurgerBuilder(), { wrapper });
    
    act(() => {
      result.current.setIngredients(mockIngredients);
    });

    const ingredient = result.current.getIngredientById(999);
    expect(ingredient).toBeUndefined();
  });

  it('should calculate total price correctly', () => {
    const { result } = renderHook(() => useBurgerBuilder(), { wrapper });
    
    act(() => {
      result.current.setIngredients(mockIngredients);
      result.current.addLayer(1); // $2.0
      result.current.addLayer(2); // $5.0
      result.current.addLayer(3); // $0.5
      result.current.addLayer(3); // $0.5
    });

    expect(result.current.getTotalPrice()).toBe(8.0);
  });

  it('should return 0 for total price with no layers', () => {
    const { result } = renderHook(() => useBurgerBuilder(), { wrapper });
    
    act(() => {
      result.current.setIngredients(mockIngredients);
    });

    expect(result.current.getTotalPrice()).toBe(0);
  });

  it('should throw error when used outside provider', () => {
    expect(() => {
      renderHook(() => useBurgerBuilder());
    }).toThrow('useBurgerBuilder must be used within a BurgerBuilderProvider');
  });
});

