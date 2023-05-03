package com.console.ticket.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SqlRequestsUtil {
    public static final String PRODUCT_FIND = """
            SELECT * FROM company.product WHERE id = ?;
            """;
    public static final String PRODUCT_FIND_ALL = """
            SELECT * FROM company.product;
            """;
    public static final String PRODUCT_DELETE = """
            DELETE FROM company.product WHERE id = ?;
            """;
    public static final String PRODUCT_SAVE = """
            INSERT INTO company.product (name, quantity, price, discount) VALUES (?, ?, ?, ?);
            """;
    public static final String PRODUCT_UPDATE = """
            UPDATE company.product
            SET name = ?,
                quantity = ?,
                price = ?,
                discount = ?
            WHERE id = ?;
            """;
    public static final String PRODUCT_GET_LIMIT = """
            SELECT * FROM company.product LIMIT ? OFFSET ?;
            """;
    public static final String CARD_FIND = """
            SELECT * FROM company.discount_card WHERE id = ?;
            """;
    public static final String CARD_FIND_ALL = """
            SELECT * FROM company.discount_card;
            """;
    public static final String CARD_DELETE = """
            DELETE FROM company.discount_card
            WHERE id = ?;
            """;
    public static final String CARD_SAVE = """
            INSERT INTO company.discount_card (discount)
            VALUES (?);
            """;
    public static final String CARD_UPDATE = """
            UPDATE company.discount_card
            SET discount = ?
            WHERE id = ?;
            """;

    public static final String CARD_GET_LIMIT = """
            SELECT * FROM company.discount_card LIMIT ? OFFSET ?;
            """;

    public static final String USER_SAVE = """
                      INSERT INTO company.users (name, email, password, role, discount_card) 
                      VALUES (?, ?, ?, ?, ?)
                      
            """;
    public static final String USER_UPDATE = """
                      UPDATE company.users 
                      SET name = ?,
                          email = ?,
                          password = ?,
                          role = ?, 
                          discount_card = ?  
                      WHERE id = ?;
            """;

    public static final String USER_FIND_BY_ID = """
            SELECT * FROM company.users WHERE id = ?;
            """;
    public static final String USER_FIND_BY_EMAIL = """
            SELECT * FROM company.users WHERE email = ?;
            """;

    public static final String USER_DELETE = """
            DELETE FROM company.users WHERE id = ?;
            """;

    public static final String USER_FIND_ALL = """
            SELECT * FROM company.users;
            """;
}
