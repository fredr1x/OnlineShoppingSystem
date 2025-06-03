CREATE TABLE users
(
    id                 BIGSERIAL PRIMARY KEY,
    first_name         VARCHAR(128) NOT NULL,
    last_name          VARCHAR(128) NOT NULL,
    email              VARCHAR(256) NOT NULL UNIQUE,
    password           VARCHAR(512) NOT NULL,
    role               VARCHAR(64)  NOT NULL,
    registered_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    profile_image_path TEXT
);

CREATE TABLE products
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(512) UNIQUE NOT NULL,
    description TEXT                NOT NULL,
    category    VARCHAR(128)        NOT NULL,
    price       NUMERIC(10, 2)      NOT NULL,
    stock       INT       DEFAULT 0 CHECK (stock >= 0),
    image_path  TEXT                NOT NULL,
    rating      INT CHECK ( rating BETWEEN 0 AND 5),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);