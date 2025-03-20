CREATE TABLE product_images
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    product_id BIGINT NOT NULL,
    image_url  VARCHAR(255) NULL,
    CONSTRAINT pk_product_images PRIMARY KEY (id)
);

ALTER TABLE product_images
    ADD CONSTRAINT FK_PRODUCT_IMAGES_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE products
DROP
COLUMN image_url;