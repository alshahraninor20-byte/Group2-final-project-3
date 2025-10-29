import { describe, it, expect, vi, beforeEach } from 'vitest';
import axios from 'axios';
import type { Ingredient, CartItem, Order, IngredientsResponse } from '../types';

// Mock axios
vi.mock('axios');

const mockedAxios = vi.mocked(axios, true);

// Create a mock axios instance
const mockAxiosInstance = {
  get: vi.fn(),
  post: vi.fn(),
  delete: vi.fn(),
};

// Setup axios.create to return our mock instance
mockedAxios.create.mockReturnValue(mockAxiosInstance as any);

// Import after mocking
const {
  getIngredients,
  getIngredientsByCategory,
  addToCart,
  getCart,
  removeCartItem,
  createOrder,
  getOrder,
} = await import('./api');

describe('API Service', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('getIngredients', () => {
    it('should fetch all ingredients', async () => {
      const mockResponse: IngredientsResponse = {
        buns: [],
        patties: [],
        toppings: [],
        sauces: [],
      };

      mockAxiosInstance.get.mockResolvedValue({ data: mockResponse });

      const result = await getIngredients();
      expect(result).toEqual(mockResponse);
      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/api/ingredients');
    });
  });

  describe('getIngredientsByCategory', () => {
    it('should fetch ingredients by category', async () => {
      const mockIngredients: Ingredient[] = [
        {
          id: 1,
          name: 'Beef Patty',
          category: 'patties',
          price: 5.99,
          imageUrl: 'patty.jpg',
        },
      ];

      mockAxiosInstance.get.mockResolvedValue({ data: mockIngredients });

      const result = await getIngredientsByCategory('patties');
      expect(result).toEqual(mockIngredients);
      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/api/ingredients/patties');
    });
  });

  describe('addToCart', () => {
    it('should add item to cart', async () => {
      const mockItem = {
        sessionId: 'session_123',
        ingredientId: 1,
        quantity: 1,
      };

      const mockResponse: CartItem = {
        id: 1,
        layers: [{ ingredientId: 1, quantity: 2 }],
        totalPrice: 10.99,
        quantity: mockItem.quantity,
      };

      mockAxiosInstance.post.mockResolvedValue({ data: mockResponse });

      const result = await addToCart(mockItem);
      expect(result).toEqual(mockResponse);
      expect(mockAxiosInstance.post).toHaveBeenCalledWith('/api/cart/items', mockItem);
    });
  });

  describe('getCart', () => {
    it('should fetch cart items by session id', async () => {
      const sessionId = 'test_session_123';
      const mockCart: CartItem[] = [
        {
          id: 1,
          layers: [{ ingredientId: 1, quantity: 2 }],
          totalPrice: 10.99,
          quantity: 1,
        },
      ];

      mockAxiosInstance.get.mockResolvedValue({ data: mockCart });

      const result = await getCart(sessionId);
      expect(result).toEqual(mockCart);
      expect(mockAxiosInstance.get).toHaveBeenCalledWith(`/api/cart/${sessionId}`);
    });
  });

  describe('removeCartItem', () => {
    it('should remove item from cart', async () => {
      const itemId = 1;

      mockAxiosInstance.delete.mockResolvedValue({});

      await removeCartItem(itemId);
      expect(mockAxiosInstance.delete).toHaveBeenCalledWith(`/api/cart/items/${itemId}`);
    });
  });

  describe('createOrder', () => {
    it('should create a new order', async () => {
      const orderData = {
        sessionId: 'session_123',
        customerName: 'John Doe',
        customerEmail: 'john@example.com',
        customerPhone: '1234567890',
        cartItemIds: [1, 2],
      };

      const mockOrder: Order = {
        id: 1,
        orderNumber: 'order_123',
        customerName: 'John Doe',
        customerEmail: 'john@example.com',
        customerPhone: '1234567890',
        totalAmount: 10.99,
        status: 'PENDING',
        createdAt: new Date().toISOString(),
      };

      mockAxiosInstance.post.mockResolvedValue({ data: mockOrder });

      const result = await createOrder(orderData);
      expect(result).toEqual(mockOrder);
      expect(mockAxiosInstance.post).toHaveBeenCalledWith('/api/orders', orderData);
    });
  });

  describe('getOrder', () => {
    it('should fetch order by id', async () => {
      const orderId = 'order_123';
      const mockOrder: Order = {
        id: 1,
        orderNumber: orderId,
        customerName: 'John Doe',
        customerEmail: 'john@example.com',
        customerPhone: '1234567890',
        totalAmount: 10.99,
        status: 'DELIVERED',
        createdAt: new Date().toISOString(),
      };

      mockAxiosInstance.get.mockResolvedValue({ data: mockOrder });

      const result = await getOrder(orderId);
      expect(result).toEqual(mockOrder);
      expect(mockAxiosInstance.get).toHaveBeenCalledWith(`/api/orders/${orderId}`);
    });
  });
});

