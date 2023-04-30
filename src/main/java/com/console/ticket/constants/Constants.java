package com.console.ticket.constants;

import java.util.regex.Pattern;

public final class Constants {
    public static int INCREMENTED_CASHIER_NUMBER = 0;
    public static final int DISCOUNT_AFTER = 5;
    public static final double PRODUCT_DISCOUNT = 10;
    public static final String OUTPUT_LINE = "_______________________________________\n";
    public static final String RECEIPT_MESSAGE = "Для вывода чека введите номер и количество товара, а также номер дисконтной карты";
    public static final String READ_MESSAGE = "Для чтения чека введите путь к файлу";
    public static final String EXIT_MESSAGE = "Для выхода из программы введите 'exit'";
    public static final String MENU_MESSAGE = String.format("%nRECEIPT BUILDER: %n%s%n%s%n%s%n: ", RECEIPT_MESSAGE, READ_MESSAGE, EXIT_MESSAGE);
    public static final String NUMBER_SYMBOL = "\u2116";
    public static final Pattern isDigit = Pattern.compile("\\d+");

//    public static final String CREATE_TABLES = """
//            CREATE SCHEMA IF NOT EXISTS company;
//            CREATE TABLE IF NOT EXISTS company.product
//            (
//                id       SERIAL PRIMARY KEY,
//                name     VARCHAR(32) UNIQUE NOT NULL,
//                quantity INT,
//                price    DECIMAL            NOT NULL,
//                discount boolean
//            );
//            CREATE TABLE IF NOT EXISTS company.discount_card
//            (
//                id       INT UNIQUE         NOT NULL,
//                discount DECIMAL            NOT NULL
//            );
//            INSERT INTO company.product (name, price, discount)
//            values ('Apple', 1.19, false),
//                   ('Banana', 2.49, false),
//                   ('Fish', 5.99, true),
//                   ('Cheese', 7.29, true),
//                   ('Chocolate', 8.29, true),
//                   ('Beef Steak', 17.99, true),
//                   ('Chocolate Milk', 4.99, true)
//                    ON CONFLICT DO NOTHING;
//
//            INSERT INTO company.discount_card (id, discount)
//            VALUES (1111, 0.1),
//                   (2222, 0.2),
//                   (3333, 0.3),
//                   (4444, 4)
//                    ON CONFLICT DO NOTHING;
//                        """;
}
