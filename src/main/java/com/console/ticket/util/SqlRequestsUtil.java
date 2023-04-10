package com.console.ticket.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SqlRequestsUtil {
    public static String PRODUCT_FIND = """
            SELECT * FROM company.product WHERE id = ?
            """;
    public static String PRODUCT_FIND_ALL = """
            SELECT * FROM company.product
            """;
    public static String PRODUCT_DELETE = """
            DELETE FROM company.product WHERE id = ?
            """;
    public static String PRODUCT_SAVE = """
            INSERT INTO company.product (name, quantity, price, discount) VALUES (?, ?, ?, ?);
            """;
    public static String PRODUCT_UPDATE = """
            UPDATE company.product
            SET name = ?,
                quantity = ?,
                price = ?,
                discount = ?
            WHERE id = ?
            """;
    public static String CARD_FIND = """
            SELECT * FROM company.discount_card WHERE id = ?
            """;
    public static String CARD_FIND_ALL = """
            SELECT * FROM company.discount_card
            """;
    public static String CARD_DELETE = """
            DELETE FROM company.discount_card
            WHERE id = ?
            """;
    public static String CARD_SAVE = """
            INSERT INTO company.discount_card (discount)
            VALUES (?);
            """;
    public static String CARD_UPDATE = """
            UPDATE company.discount_card
            SET discount = ?
            WHERE id = ?
            """;
}
