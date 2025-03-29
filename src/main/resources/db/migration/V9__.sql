ALTER TABLE brand
    ADD CONSTRAINT uc_brand_name UNIQUE (name);

ALTER TABLE products
    MODIFY brand_id BIGINT NOT NULL;