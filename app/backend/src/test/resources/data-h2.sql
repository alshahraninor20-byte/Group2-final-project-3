-- Test data for H2 database (simplified version)
-- Only insert data if table is empty (one-time initialization)

-- Insert ingredients using H2-compatible syntax
INSERT INTO ingredients (name, category, price, description, image_url, is_available, sort_order) 
SELECT 'Classic Sesame Bun', 'buns', 1.50, 'Fresh sesame seed bun', '/images/buns/sesame-bun.jpg', true, 1
WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE name = 'Classic Sesame Bun');

INSERT INTO ingredients (name, category, price, description, image_url, is_available, sort_order) 
SELECT 'Beef Patty', 'patties', 4.50, 'Juicy 100% beef patty', '/images/patties/beef-patty.jpg', true, 1
WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE name = 'Beef Patty');

INSERT INTO ingredients (name, category, price, description, image_url, is_available, sort_order) 
SELECT 'Lettuce', 'toppings', 0.50, 'Fresh crisp lettuce', '/images/toppings/lettuce.jpg', true, 1
WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE name = 'Lettuce');

INSERT INTO ingredients (name, category, price, description, image_url, is_available, sort_order) 
SELECT 'Ketchup', 'sauces', 0.25, 'Classic tomato ketchup', '/images/sauces/ketchup.jpg', true, 1
WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE name = 'Ketchup');
