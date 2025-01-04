CREATE TABLE colors
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    name      VARCHAR(255) NULL,
    image_url VARCHAR(255) NOT NULL,
    CONSTRAINT pk_colors PRIMARY KEY (id)
);

CREATE TABLE main_categories
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    name      VARCHAR(255) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    CONSTRAINT pk_main_categories PRIMARY KEY (id)
);

CREATE TABLE nested_categories
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    subcategory_id BIGINT       NOT NULL,
    name           VARCHAR(255) NOT NULL,
    image_url      VARCHAR(255) NOT NULL,
    CONSTRAINT pk_nested_categories PRIMARY KEY (id)
);

CREATE TABLE order_items
(
    quantity_ordered INT NULL,
    order_id         BIGINT NOT NULL,
    product_id       BIGINT NOT NULL,
    CONSTRAINT pk_order_items PRIMARY KEY (order_id, product_id)
);

CREATE TABLE orders
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    full_name  VARCHAR(255) NOT NULL,
    phone      VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NULL,
    address    VARCHAR(255) NULL,
    order_date datetime NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE product_color
(
    color_id   BIGINT NOT NULL,
    product_id BIGINT NOT NULL
);

CREATE TABLE products
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    name               VARCHAR(255)  NOT NULL,
    `description`      TEXT NULL,
    price              DECIMAL(8, 2) NOT NULL,
    discount           INT NULL,
    image_url          VARCHAR(255) NULL,
    stock_quantity     INT           NOT NULL,
    brand              VARCHAR(255) NULL,
    nested_category_id BIGINT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
);

CREATE TABLE subcategories
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    main_category_id BIGINT       NOT NULL,
    name             VARCHAR(255) NOT NULL,
    imageurl         VARCHAR(255) NOT NULL,
    CONSTRAINT pk_subcategories PRIMARY KEY (id)
);

ALTER TABLE nested_categories
    ADD CONSTRAINT FK_NESTED_CATEGORIES_ON_SUBCATEGORY FOREIGN KEY (subcategory_id) REFERENCES subcategories (id);

ALTER TABLE order_items
    ADD CONSTRAINT FK_ORDER_ITEMS_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE order_items
    ADD CONSTRAINT FK_ORDER_ITEMS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_NESTED_CATEGORY FOREIGN KEY (nested_category_id) REFERENCES nested_categories (id);

ALTER TABLE subcategories
    ADD CONSTRAINT FK_SUBCATEGORIES_ON_MAIN_CATEGORY FOREIGN KEY (main_category_id) REFERENCES main_categories (id);

ALTER TABLE product_color
    ADD CONSTRAINT fk_procol_on_color FOREIGN KEY (color_id) REFERENCES colors (id);

ALTER TABLE product_color
    ADD CONSTRAINT fk_procol_on_product FOREIGN KEY (product_id) REFERENCES products (id);