package com.console.ticket.db;

import data.DataBase;
import entity.product.Product;
import entity.product.ValidateProductBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDbTest {
    private static final DataBase dataBase = DataBase.getInstance();
    private static final Product CHEESE = new ValidateProductBuilder()
            .setId(4)
            .setName("Cheese")
            .setQuantity(1)
            .setPrice(7.29)
            .setDiscount(true)
            .build();

    @Test
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
    static void clearList(){
         dataBase.getProductsList().clear();
    }
}

