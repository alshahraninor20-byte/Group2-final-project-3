-- Database schema for Burger Builder application (SQL Server)

-- Create ingredients table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ingredients' AND xtype='U')
CREATE TABLE ingredients (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    category NVARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price > 0),
    description NVARCHAR(500),
    image_url NVARCHAR(255),
    is_available BIT NOT NULL DEFAULT 1,
    sort_order INT
);

-- Create cart_items table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='cart_items' AND xtype='U')
CREATE TABLE cart_items (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    session_id NVARCHAR(255) NOT NULL,
    ingredient_id BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10,2),
    total_price DECIMAL(10,2),
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

-- Create burger_layers table (junction table for custom burger layers)
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='burger_layers' AND xtype='U')
CREATE TABLE burger_layers (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    cart_item_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    layer_order INT NOT NULL CHECK (layer_order > 0),
    quantity INT NOT NULL DEFAULT 1 CHECK (quantity > 0),
    unit_price DECIMAL(10,2),
    FOREIGN KEY (cart_item_id) REFERENCES cart_items(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

-- Create orders table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='orders' AND xtype='U')
CREATE TABLE orders (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_number NVARCHAR(50) NOT NULL UNIQUE,
    customer_name NVARCHAR(100) NOT NULL,
    customer_email NVARCHAR(100),
    customer_phone NVARCHAR(20),
    total_amount DECIMAL(10,2) NOT NULL CHECK (total_amount > 0),
    status NVARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2
);

-- Create order_items table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='order_items' AND xtype='U')
CREATE TABLE order_items (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10,2),
    total_price DECIMAL(10,2),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

-- Create order_layers table (junction table for order burger layers)
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='order_layers' AND xtype='U')
CREATE TABLE order_layers (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_item_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    layer_order INT NOT NULL CHECK (layer_order > 0),
    quantity INT NOT NULL DEFAULT 1 CHECK (quantity > 0),
    unit_price DECIMAL(10,2),
    FOREIGN KEY (order_item_id) REFERENCES order_items(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

-- Create indexes for better performance
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_ingredients_category')
CREATE INDEX idx_ingredients_category ON ingredients(category);
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_ingredients_available')
CREATE INDEX idx_ingredients_available ON ingredients(is_available);
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_cart_items_session')
CREATE INDEX idx_cart_items_session ON cart_items(session_id);
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_cart_items_ingredient')
CREATE INDEX idx_cart_items_ingredient ON cart_items(ingredient_id);
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_orders_customer_email')
CREATE INDEX idx_orders_customer_email ON orders(customer_email);
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_orders_status')
CREATE INDEX idx_orders_status ON orders(status);
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_orders_created_at')
CREATE INDEX idx_orders_created_at ON orders(created_at);
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_order_items_order_id')
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_order_items_ingredient_id')
CREATE INDEX idx_order_items_ingredient_id ON order_items(ingredient_id);
