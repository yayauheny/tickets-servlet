package com.console.ticket.service;

import entity.product.ValidateProductBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class InvalidInputTest {
    @Test
    @Disabled
    void checkException() {
        assertThrows(RuntimeException.class, () -> new ValidateProductBuilder().setId(-1).setQuantity(2).build(), "Id of product is invalid");
        assertThrows(RuntimeException.class, () -> new ValidateProductBuilder().setId(0).setQuantity(-1).build(), "Quantity of product is invalid");

    }
}
