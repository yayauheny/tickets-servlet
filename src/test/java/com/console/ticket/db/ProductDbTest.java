package com.console.ticket.db;

import data.DataBase;
import entity.Product;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductDbTest {
    private static final DataBase dataBase = DataBase.getInstance();
    private static final Product CHEESE = Product.builder()
            .id(4)
            .name("Cheese")
            .quantity(1)
            .price(7.29)
            .isDiscount(true)
            .build();

    @Test
    @Disabled

    void findProduct() {
        dataBase.getProductsList().clear();
        dataBase.findProductById(CHEESE.getId(), CHEESE.getQuantity());
        Product dbProduct = dataBase.getProductsList().get(0);

        assertEquals(dbProduct.getId(), CHEESE.getId());
        assertEquals(dbProduct.getName(), CHEESE.getName());
        assertEquals(dbProduct.getPrice(), CHEESE.getPrice());
        assertEquals(dbProduct.getQuantity(), CHEESE.getQuantity());
    }
    @AfterAll
    @Disabled
    static void clearList(){
         dataBase.getProductsList().clear();
    }
}

