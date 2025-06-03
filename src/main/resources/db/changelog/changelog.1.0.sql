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
    rating      INT CHECK ( rating BETWEEN 1 AND 5),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE carts
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT REFERENCES users (id),
    total_price NUMERIC(10, 2) DEFAULT 0 NOT NULL,
    created_at  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cart_items
(
    id         BIGSERIAL PRIMARY KEY,
    cart_id    BIGINT REFERENCES carts (id),
    product_id BIGINT REFERENCES products (id),
    quantity   INT DEFAULT 1 CHECK ( quantity >= 1 )
);

CREATE TABLE orders
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGSERIAL REFERENCES users (id),
    total_price NUMERIC(10, 2) NOT NULL,
    status      VARCHAR(32)    NOT NULL DEFAULT 'PENDING',
    created_at  TIMESTAMP               DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_items
(
    id                BIGSERIAL PRIMARY KEY,
    order_id          BIGINT REFERENCES orders (id),
    product_id        BIGINT REFERENCES products (id),
    quantity          INT            NOT NULL CHECK ( quantity >= 1 ),
    price_at_purchase NUMERIC(10, 2) NOT NULL
);

CREATE TABLE wish_list
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES users (id),
    product_id BIGINT REFERENCES products (id),
    added_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, product_id)
);

CREATE TABLE payments
(
    id             BIGSERIAL PRIMARY KEY,
    order_id       BIGINT REFERENCES orders (id) UNIQUE,
    payment_method VARCHAR(64)    NOT NULL,
    amount         NUMERIC(10, 2) NOT NULL,
    status         VARCHAR(32) DEFAULT 'PENDING',
    paid_at        TIMESTAMP,
    created_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reviews
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES users (id),
    product_id BIGINT REFERENCES products (id),
    rating     INT CHECK (rating BETWEEN 1 AND 5),
    comment    TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

