--liquibase formatted sql

--changeset yayauheny:3
CREATE TABLE IF NOT EXISTS company.discount_card
(
    id       SERIAL PRIMARY KEY,
    discount DECIMAL NOT NULL
);