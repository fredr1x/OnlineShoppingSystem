ALTER TABLE carts
ADD COLUMN total_items INTEGER DEFAULT 0 NOT NULL CHECK ( total_items >= 0 );