ALTER TABLE wish_list
DROP COLUMN product_id;

ALTER TABLE wish_list
DROP COLUMN added_at;

CREATE TABLE wish_list_item
(
    id BIGSERIAL PRIMARY KEY,
    wish_list_id BIGINT REFERENCES wish_list(id),
    product_id BIGINT REFERENCES products(id),
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);