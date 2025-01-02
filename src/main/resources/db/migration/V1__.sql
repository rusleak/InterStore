CREATE TABLE cart
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    product_id BIGINT NULL,
    quantity   INT NULL,
    CONSTRAINT pk_cart PRIMARY KEY (id)
);

CREATE TABLE categories
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    name     VARCHAR(255) NOT NULL,
    imageurl VARCHAR(255) NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE order_items
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    order_id   BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT NULL,
    price      DECIMAL NULL,
    CONSTRAINT pk_order_items PRIMARY KEY (id)
);

CREATE TABLE orders
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255) NOT NULL,
    phone      VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NULL,
    address    TEXT NULL,
    order_date date NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE products
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    category_id    BIGINT       NOT NULL,
    subcategory_id BIGINT       NOT NULL,
    name           VARCHAR(255) NOT NULL,
    `description`  TEXT NULL,
    color          VARCHAR(255) NULL,
    price          DECIMAL NULL,
    discounted     BIT(1) NULL,
    image_url      VARCHAR(255) NULL,
    stock_quantity INT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
);

CREATE TABLE subcategories
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    category_id BIGINT       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    imageurl    VARCHAR(255) NOT NULL,
    CONSTRAINT pk_subcategories PRIMARY KEY (id)
);

ALTER TABLE cart
    ADD CONSTRAINT FK_CART_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE order_items
    ADD CONSTRAINT FK_ORDER_ITEMS_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE order_items
    ADD CONSTRAINT FK_ORDER_ITEMS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_SUBCATEGORY FOREIGN KEY (subcategory_id) REFERENCES subcategories (id);

ALTER TABLE subcategories
    ADD CONSTRAINT FK_SUBCATEGORIES_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);