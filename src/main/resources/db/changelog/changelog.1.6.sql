ALTER TABLE carts
ALTER COLUMN total_price
SET DEFAULT 0;

ALTER TABLE cart_items
ADD UNIQUE (cart_id, product_id);