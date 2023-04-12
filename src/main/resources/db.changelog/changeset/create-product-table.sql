--liquibase formatted sql

--changeset yayauheny:2
CREATE TABLE IF NOT EXISTS company.product
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(32) UNIQUE NOT NULL,
    quantity INT,
    price    DECIMAL            NOT NULL,
    discount boolean
);