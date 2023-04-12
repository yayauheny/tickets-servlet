--liquibase formatted sql

--changeset yayauheny:4
INSERT INTO company.product (name, price, discount)
values ('Apple', 1.19, false),
       ('Banana', 2.49, false),
       ('Fish', 5.99, true),
       ('Cheese', 7.29, true),
       ('Chocolate', 8.29, true),
       ('Beef Steak', 17.99, true),
       ('Chocolate Milk', 4.99, true)
ON CONFLICT DO NOTHING;