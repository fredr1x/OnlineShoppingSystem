CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    role VARCHAR(128) NOT NULL
);

CREATE TABLE user_roles
(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    role_id INTEGER REFERENCES roles(id)
);

ALTER TABLE users
DROP COLUMN role;