--liquibase formatted sql

--changeset yayauheny:5
INSERT INTO company.discount_card (discount)
VALUES (0.1),
       (0.2),
       (0.6),
       (1.9),
       (0.1),
       (3.3),
       (0.3),
       (0.5)
ON CONFLICT DO NOTHING;