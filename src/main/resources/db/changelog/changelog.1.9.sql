ALTER TABLE IF EXISTS products DROP CONSTRAINT products_rating_check;

ALTER TABLE IF EXISTS products
    ADD CONSTRAINT products_rating_check
        CHECK (rating >= 0 AND rating <= 5);
