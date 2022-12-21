CREATE SCHEMA IF NOT EXISTS company;
CREATE TABLE IF NOT EXISTS company.product
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(32) UNIQUE NOT NULL,
    quantity INT,
    price    DECIMAL            NOT NULL,
    discount boolean
    );
CREATE TABLE IF NOT EXISTS company.discount_card
(
    id       INT UNIQUE         NOT NULL,
    discount DECIMAL            NOT NULL
);
INSERT INTO company.product (name, price, discount)
values ('Apple', 1.19, false),
       ('Banana', 2.49, false),
       ('Fish', 5.99, true),
       ('Cheese', 7.29, true),
       ('Chocolate', 8.29, true),
       ('Beef Steak', 17.99, true),
       ('Chocolate Milk', 4.99, true)
    ON CONFLICT DO NOTHING;

INSERT INTO company.discount_card (id, discount)
VALUES (1111, 0.1),
       (2222, 0.2),
       (1122, 0.6),
       (2323, 1.9),
       (5555, 0.1),
       (4444, 3.3),
       (7777, 0.3),
       (9999, 0.5)
    ON CONFLICT DO NOTHING;