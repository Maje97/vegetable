INSERT INTO categories (name) VALUES
('Leafy greens'),
('Root vegetables'),
('Bulb vegetables'),
('Stem vegetables'),
('Flower vegetables'),
('Fruit vegetables'),
('Seed vegetables'),
('Mushrooms');

INSERT INTO products (name, description, price, stock_quantity, category_id) VALUES
('Spinach', 'Fresh organic spinach', 3.50, 200, 1),
('Potato', 'Organic potatoes', 1.00, 100, 2),
('Garlic', 'Organic garlic', 2.50, 70, 3),
('Rhubarb', 'Fresh organic rhubarbs', 3.00, 200, 4),
('Cauliflower', 'Fresh organic cauliflowers', 5.25, 50, 5),
('Cucumber', 'Fresh organic cucumbers', 1.25, 100, 6),
('Peas', 'Fresh organic peas', 0.50, 500, 7),
('Shiitake mushroom', 'Fresh organic shiitake', 2.00, 100, 8);

INSERT INTO customers (name, address, email) VALUES
('Sven Svensson', 'Example 123, 45678 Example', 'sven@example.com');

INSERT INTO orders (customer_id, order_date, total_amount) VALUES
(1, '2025-06-14 14:18:52', 3.00);

INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES
(1, 2, 3, 1.00);