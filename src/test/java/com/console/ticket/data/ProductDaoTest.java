package com.console.ticket.data;

import com.console.ticket.entity.Product;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.exception.InputException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ProductDaoTest {
    private static com.console.ticket.data.ProductDao productDao;

    @BeforeAll
    static void initialize() {
        productDao = com.console.ticket.data.ProductDao.getInstance();
    }
    @DisplayName("check if empty for not existing productId")
    @Test
    void checkFindProductByIdShouldBeEmpty() {
        Integer noSuchProductId = 909090;

        assertThat(productDao.findById(noSuchProductId)).isEmpty();
    }

//    @DisplayName("find all database products properly")
//    @Test
//    void checkFindAllProductsFromDatabaseProperly() {
//        int databaseProductsQuantity = 8;
//
//        assertThat(productDao.findAll()).size().isEqualTo(databaseProductsQuantity);
//    }

    @DisplayName("exception if id is negative")
    @Test
    void checkFindByIdThrowsInputExceptionIfNegative() {
        int negativeId = -1;

        assertThrows(InputException.class, () -> productDao.findById(negativeId));
    }

    @DisplayName("check throw exception if id is null")
    @Test
    void checkFindByIdThrowsInputExceptionIfNull() {
        assertThrows(InputException.class, () -> productDao.findById(null));
    }

    @DisplayName("check find all existing products by their id")
    @ParameterizedTest
    @MethodSource("com.console.ticket.data.ProductDaoTest#getAllProducts")
    void checkFindProductByIdReturnAllProductsFromDatabase(Optional<Product> product, Integer productId) {
        Optional<Product> foundProduct = productDao.findById(productId);

        assertThat(foundProduct.get()).isEqualTo(product.get());
    }
    @DisplayName("arguments of all database products")
    static Stream<Arguments> getAllProducts() throws DatabaseException {
        return productDao.findAll().stream()
                .map(product -> Arguments.of(product, product.get().getId()));
    }
}
