DROP TABLE IF EXISTS cart_item;

CREATE TABLE IF NOT EXISTS cart_item (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    merchant_id BIGINT NOT NULL,
    food_id BIGINT NOT NULL,
    food_name VARCHAR(64) NOT NULL,
    food_price DECIMAL(10,2) NOT NULL,
    food_image_url VARCHAR(255),
    quantity INT NOT NULL,
    checked TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_cart_user_merchant_food (user_id, merchant_id, food_id),
    KEY idx_cart_user_merchant (user_id, merchant_id)
);
