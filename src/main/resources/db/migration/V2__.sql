CREATE TABLE dimensions
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    size VARCHAR(255) NULL,
    CONSTRAINT pk_dimensions PRIMARY KEY (id)
);

CREATE TABLE product_dimension
(
    dimension_id BIGINT NOT NULL,
    product_id   BIGINT NOT NULL
);

ALTER TABLE products
    ADD CONSTRAINT uc_products_onec UNIQUE (onec_id);

ALTER TABLE product_dimension
    ADD CONSTRAINT fk_prodim_on_dimensions FOREIGN KEY (dimension_id) REFERENCES dimensions (id);

ALTER TABLE product_dimension
    ADD CONSTRAINT fk_prodim_on_product FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE products
DROP
COLUMN dimensions;

ALTER TABLE products
DROP
COLUMN discount;