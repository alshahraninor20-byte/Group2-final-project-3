import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import IngredientCard from './IngredientCard';
import type { Ingredient } from '../../types';

describe('IngredientCard', () => {
  const mockIngredient: Ingredient = {
    id: 1,
    name: 'Beef Patty',
    category: 'patties',
    price: 5.99,
    imageUrl: 'patty.jpg',
  };

  const mockOnAdd = vi.fn();

  it('should render ingredient name', () => {
    render(<IngredientCard ingredient={mockIngredient} onAdd={mockOnAdd} />);
    expect(screen.getByText('Beef Patty')).toBeInTheDocument();
  });

  it('should render ingredient price with 2 decimal places', () => {
    render(<IngredientCard ingredient={mockIngredient} onAdd={mockOnAdd} />);
    expect(screen.getByText('$5.99')).toBeInTheDocument();
  });

  it('should render correct icon for patties category', () => {
    render(<IngredientCard ingredient={mockIngredient} onAdd={mockOnAdd} />);
    expect(screen.getByText('🥩')).toBeInTheDocument();
  });

  it('should render correct icon for buns category', () => {
    const bunsIngredient = { ...mockIngredient, category: 'buns' as const };
    render(<IngredientCard ingredient={bunsIngredient} onAdd={mockOnAdd} />);
    expect(screen.getByText('🍞')).toBeInTheDocument();
  });

  it('should render correct icon for toppings category', () => {
    const toppingsIngredient = { ...mockIngredient, category: 'toppings' as const };
    render(<IngredientCard ingredient={toppingsIngredient} onAdd={mockOnAdd} />);
    expect(screen.getByText('🥬')).toBeInTheDocument();
  });

  it('should render correct icon for sauces category', () => {
    const saucesIngredient = { ...mockIngredient, category: 'sauces' as const };
    render(<IngredientCard ingredient={saucesIngredient} onAdd={mockOnAdd} />);
    expect(screen.getByText('🧂')).toBeInTheDocument();
  });

  it('should call onAdd with ingredient id when card is clicked', async () => {
    const user = userEvent.setup();
    render(<IngredientCard ingredient={mockIngredient} onAdd={mockOnAdd} />);
    
    const card = screen.getByText('Beef Patty').closest('.ingredient-card');
    await user.click(card!);

    expect(mockOnAdd).toHaveBeenCalledWith(mockIngredient.id);
    expect(mockOnAdd).toHaveBeenCalledTimes(1);
  });

  it('should call onAdd when add button is clicked', async () => {
    const user = userEvent.setup();
    render(<IngredientCard ingredient={mockIngredient} onAdd={mockOnAdd} />);
    
    const addButton = screen.getByText('Add +');
    await user.click(addButton);

    expect(mockOnAdd).toHaveBeenCalledWith(mockIngredient.id);
  });

  it('should format price with whole numbers correctly', () => {
    const wholeNumberIngredient = { ...mockIngredient, price: 3 };
    render(<IngredientCard ingredient={wholeNumberIngredient} onAdd={mockOnAdd} />);
    expect(screen.getByText('$3.00')).toBeInTheDocument();
  });
});

