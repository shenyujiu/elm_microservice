DROP TABLE IF EXISTS `user_token`;
DROP TABLE IF EXISTS `user_default_address`;

CREATE TABLE IF NOT EXISTS `user_token` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `token` VARCHAR(128) NOT NULL,
    `expired_at` DATETIME NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_token_token` (`token`),
    KEY `idx_user_token_user_id` (`user_id`)
);

CREATE TABLE IF NOT EXISTS `user_default_address` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `address_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_default_address_user_id` (`user_id`)
);
