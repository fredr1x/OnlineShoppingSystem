ALTER TABLE products DROP CONSTRAINT products_category_check;

ALTER TABLE products
ALTER COLUMN category
TYPE VARCHAR(255)
USING category::VARCHAR;