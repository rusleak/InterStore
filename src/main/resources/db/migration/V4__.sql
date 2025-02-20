-- Удаляем старую таблицу, если она есть
DROP TABLE IF EXISTS product_images;

-- Создаём таблицу с составным первичным ключом
CREATE TABLE product_images (
                                product_id BIGINT NOT NULL,
                                image_url VARCHAR(255) NOT NULL,
                                PRIMARY KEY (product_id, image_url),
                                CONSTRAINT FK_PRODUCT_IMAGES_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
