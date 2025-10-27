-- Sample data for Burger Builder application (PostgreSQL)
-- Only insert data if table is empty (one-time initialization)

-- Check if ingredients table has data, if not insert default ingredients
INSERT INTO ingredients (name, category, price, description, image_url, is_available, sort_order)
SELECT * FROM (VALUES
    -- Buns
    ('Classic Sesame Bun', 'buns', 1.50, 'Fresh sesame seed bun', '/images/buns/sesame-bun.jpg', true, 1),
    ('Whole Wheat Bun', 'buns', 1.75, 'Healthy whole wheat bun', '/images/buns/whole-wheat-bun.jpg', true, 2),
    ('Brioche Bun', 'buns', 2.00, 'Rich and buttery brioche bun', '/images/buns/brioche-bun.jpg', true, 3),
    ('Gluten-Free Bun', 'buns', 2.25, 'Gluten-free alternative bun', '/images/buns/gluten-free-bun.jpg', true, 4),
    -- Patties
    ('Beef Patty', 'patties', 4.50, 'Juicy 100% beef patty', '/images/patties/beef-patty.jpg', true, 1),
    ('Chicken Breast', 'patties', 4.25, 'Grilled chicken breast', '/images/patties/chicken-breast.jpg', true, 2),
    ('Turkey Patty', 'patties', 4.00, 'Lean turkey patty', '/images/patties/turkey-patty.jpg', true, 3),
    ('Veggie Patty', 'patties', 3.75, 'Plant-based veggie patty', '/images/patties/veggie-patty.jpg', true, 4),
    ('Salmon Patty', 'patties', 5.50, 'Fresh salmon patty', '/images/patties/salmon-patty.jpg', true, 5),
    -- Toppings
    ('Lettuce', 'toppings', 0.50, 'Fresh crisp lettuce', '/images/toppings/lettuce.jpg', true, 1),
    ('Tomato', 'toppings', 0.75, 'Fresh tomato slices', '/images/toppings/tomato.jpg', true, 2),
    ('Onion', 'toppings', 0.50, 'Raw or grilled onions', '/images/toppings/onion.jpg', true, 3),
    ('Pickles', 'toppings', 0.50, 'Dill pickle slices', '/images/toppings/pickles.jpg', true, 4),
    ('Mushrooms', 'toppings', 1.00, 'Sautéed mushrooms', '/images/toppings/mushrooms.jpg', true, 5),
    ('Bacon', 'toppings', 1.50, 'Crispy bacon strips', '/images/toppings/bacon.jpg', true, 6),
    ('Avocado', 'toppings', 1.25, 'Fresh avocado slices', '/images/toppings/avocado.jpg', true, 7),
    ('Jalapeños', 'toppings', 0.75, 'Spicy jalapeño peppers', '/images/toppings/jalapenos.jpg', true, 8),
    -- Sauces
    ('Ketchup', 'sauces', 0.25, 'Classic tomato ketchup', '/images/sauces/ketchup.jpg', true, 1),
    ('Mustard', 'sauces', 0.25, 'Yellow mustard', '/images/sauces/mustard.jpg', true, 2),
    ('Mayo', 'sauces', 0.25, 'Creamy mayonnaise', '/images/sauces/mayo.jpg', true, 3),
    ('BBQ Sauce', 'sauces', 0.50, 'Smoky BBQ sauce', '/images/sauces/bbq-sauce.jpg', true, 4),
    ('Ranch', 'sauces', 0.50, 'Creamy ranch dressing', '/images/sauces/ranch.jpg', true, 5),
    ('Sriracha', 'sauces', 0.50, 'Spicy sriracha sauce', '/images/sauces/sriracha.jpg', true, 6),
    ('Aioli', 'sauces', 0.75, 'Garlic aioli', '/images/sauces/aioli.jpg', true, 7),
    ('Buffalo Sauce', 'sauces', 0.50, 'Spicy buffalo sauce', '/images/sauces/buffalo-sauce.jpg', true, 8)
) AS v(name, category, price, description, image_url, is_available, sort_order)
WHERE NOT EXISTS (SELECT 1 FROM ingredients LIMIT 1);
