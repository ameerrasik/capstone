-- AmeerRasik Mart Seed Data

-- Passwords below are bcrypt hashes for:
-- Admin: 'Admin@123'
-- Sellers: 'Seller@123'
-- Buyers: 'Buyer@123'

INSERT INTO users (id, name, email, password_hash, role) VALUES
(1, 'AmeerRasik Admin', 'admin@ameerrasikmart.com', '$2a$10$G0N3f3y1CqK9u0j5O0S5.eJ5Z6y7u8i9o0p1a2b3c4d5e6f7g8h9i', 'ADMIN'),
(2, 'TechStore Electronics', 'techstore@ameerrasikmart.com', '$2a$10$G0N3f3y1CqK9u0j5O0S5.eJ5Z6y7u8i9o0p1a2b3c4d5e6f7g8h9i', 'SELLER'),
(3, 'FashionHub India', 'fashionhub@ameerrasikmart.com', '$2a$10$G0N3f3y1CqK9u0j5O0S5.eJ5Z6y7u8i9o0p1a2b3c4d5e6f7g8h9i', 'SELLER'),
(4, 'Demo Buyer', 'buyer@ameerrasikmart.com', '$2a$10$G0N3f3y1CqK9u0j5O0S5.eJ5Z6y7u8i9o0p1a2b3c4d5e6f7g8h9i', 'BUYER'),
(5, 'Rahul Sharma', 'rahul@gmail.com', '$2a$10$G0N3f3y1CqK9u0j5O0S5.eJ5Z6y7u8i9o0p1a2b3c4d5e6f7g8h9i', 'BUYER'),
(6, 'Priya Patel', 'priya@gmail.com', '$2a$10$G0N3f3y1CqK9u0j5O0S5.eJ5Z6y7u8i9o0p1a2b3c4d5e6f7g8h9i', 'BUYER');

INSERT INTO products (id, seller_id, name, description, price, stock_qty, category, image_url) VALUES
(1, 2, 'Ergonomic Wireless Mouse', '2.4GHz High Precision Optical Mouse with Silent Click and Rechargeable Battery.', 1299.00, 45, 'Electronics', 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=600&auto=format&fit=crop&q=80'),
(2, 2, 'Mechanical Gaming Keyboard', 'RGB Backlit Mechanical Keyboard with Tactile Blue Switches and Detachable Cable.', 3499.00, 20, 'Electronics', 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop&q=80'),
(3, 2, 'Multi-Port USB-C Hub 7-in-1', 'Aluminum Type-C Adapter with 4K HDMI, 100W Power Delivery, SD Card Reader, and 3x USB 3.0.', 2199.00, 30, 'Electronics', 'https://images.unsplash.com/photo-1616440347437-b1c73416efc2?w=600&auto=format&fit=crop&q=80'),
(4, 2, 'Portable Bluetooth Speaker', 'Deep Bass 20W Wireless Speaker with IPX7 Waterproofing and 12-Hour Playtime.', 1899.00, 25, 'Electronics', 'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=600&auto=format&fit=crop&q=80'),
(5, 2, 'Aluminum Laptop Stand', 'Foldable Ergonomic Laptop Holder with Heat Dissipation Ventilation for Mac & Windows.', 999.00, 50, 'Electronics', 'https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?w=600&auto=format&fit=crop&q=80'),
(6, 2, 'Active Noise Cancelling Headphones', 'Over-Ear Wireless Headphones with Hybrid ANC, High-Res Audio, and 40H Battery Life.', 4999.00, 15, 'Electronics', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80'),

(7, 3, 'Water-Resistant Laptop Backpack', '15.6 Inch Premium Travel Laptop Bag with USB Charging Port and Anti-Theft Pocket.', 1499.00, 40, 'Accessories', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600&auto=format&fit=crop&q=80'),
(8, 3, 'Classic Cotton Hoodie', 'Unisex Heavyweight Fleece Hoodie with Kanga Pocket and Ribbed Cuffs.', 1299.00, 60, 'Fashion', 'https://images.unsplash.com/photo-1556905055-8f358a7a47b2?w=600&auto=format&fit=crop&q=80'),
(9, 3, 'Slim Fit Denim Jacket', 'Vintage Wash 100% Premium Cotton Denim Jacket with Button Closure.', 2499.00, 18, 'Fashion', 'https://images.unsplash.com/photo-1576995853123-5a10305d93c0?w=600&auto=format&fit=crop&q=80'),
(10, 3, 'Minimalist Leather Wallet', 'RFID Blocking Genuine Leather Bifold Wallet with Coin Pocket and Card Slots.', 799.00, 35, 'Accessories', 'https://images.unsplash.com/photo-1627123424574-724758594e93?w=600&auto=format&fit=crop&q=80'),

(11, 2, 'Smart Fitness Watch', 'AMOLED Display Smartwatch with SpO2 Monitor, Heart Rate Tracker, and 100+ Sports Modes.', 2999.00, 22, 'Electronics', 'https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=600&auto=format&fit=crop&q=80'),
(12, 3, 'Stainless Steel Thermal Water Bottle', 'Double-Wall Vacuum Insulated 750ml Flask Keeps Water Cold for 24 Hours.', 649.00, 80, 'Home', 'https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=600&auto=format&fit=crop&q=80'),
(13, 3, 'LED Eye-Care Desk Lamp', 'Dimmable Touch Control Study Light with 5 Color Modes and Timer Function.', 1199.00, 28, 'Home', 'https://images.unsplash.com/photo-1534073828943-f801091bb18c?w=600&auto=format&fit=crop&q=80'),
(14, 3, 'Hardcover Executive Journal Notebook', '200 Pages 120GSM Thick Ruled Paper Notebook with Bookmark and Pen Loop.', 499.00, 100, 'Books', 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop&q=80'),
(15, 3, 'The Java Architecture Guide (Book)', 'Mastering Enterprise Java Web Applications, Servlets, JDBC, and Clean Design Patterns.', 899.00, 45, 'Books', 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=600&auto=format&fit=crop&q=80');

INSERT INTO reviews (product_id, user_id, rating, comment) VALUES
(1, 4, 5, 'Exceptional quality mouse. Very responsive and smooth on desk.'),
(2, 5, 5, 'Great mechanical tactile feel! Typing fast is so enjoyable.'),
(6, 6, 4, 'Noise cancelling works surprisingly well during travel.');

INSERT INTO wishlist_items (id, user_id, product_id) VALUES
(1, 4, 2),
(2, 4, 7);

