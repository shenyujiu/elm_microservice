DROP TABLE IF EXISTS payment_log;

CREATE TABLE IF NOT EXISTS payment_log (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    order_no VARCHAR(64) NOT NULL,
    pay_channel VARCHAR(32) NOT NULL,
    pay_status VARCHAR(16) NOT NULL,
    transaction_no VARCHAR(128),
    amount DECIMAL(10,2) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    callback_at DATETIME NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_payment_order_id (order_id)
);
