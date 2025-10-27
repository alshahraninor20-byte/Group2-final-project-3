export interface Ingredient {
  id: number;
  name: string;
  category: 'buns' | 'patties' | 'toppings' | 'sauces';
  price: number;
  imageUrl: string;
}

export interface BurgerLayer {
  ingredientId: number;
  quantity: number;
}

export interface CartItem {
  id: number;
  layers: BurgerLayer[];
  totalPrice: number;
  quantity: number;
}

export interface Order {
  id: number;
  orderNumber: string;
  customerName: string;
  customerEmail: string;
  customerPhone: string;
  totalAmount: number;
  status: 'PENDING' | 'CONFIRMED' | 'PREPARING' | 'READY' | 'DELIVERED' | 'CANCELLED';
  createdAt: string;
  updatedAt?: string;
  orderItems?: OrderItem[];
}

export interface OrderItem {
  id: number;
  ingredientId: number;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}

export interface CustomerDetails {
  name: string;
  email: string;
  phone: string;
  address: string;
}

export interface IngredientsResponse {
  patties: Ingredient[];
  toppings: Ingredient[];
  sauces: Ingredient[];
  buns: Ingredient[];
}

export type IngredientCategory = 'buns' | 'patties' | 'toppings' | 'sauces';

