package com.console.ticket.service.impl;

import com.console.ticket.data.ProductDao;
import com.console.ticket.entity.Product;
import com.console.ticket.util.ProductTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductDao productDao;

    @DisplayName("assert that service return cached product from cache")
    @Test
    void checkGetByIdReturnProductFromCache() {
        Optional<Product> expectedProduct = Optional.of(ProductTestBuilder.aProduct()
                .withId(1)
                .withName("Bread")
                .build());
        Integer id = expectedProduct.get().getId();

        doReturn(expectedProduct, (Object) null, (Object) null).when(productDao).findById(id);

        productService.findById(id);
        productService.findById(id);
        Optional<Product> actualProduct = productService.findById(id);

        assertThat(actualProduct).isEqualTo(expectedProduct);
    }

    @DisplayName("assert that service return null when product not exists")
    @Test
    void checkGetByIdReturnNull() {
        Optional<Product> expectedProduct = Optional.of(ProductTestBuilder.aProduct()
                .withId(1)
                .build());
        Integer id = expectedProduct.get().getId();
        doReturn(null).when(productDao).findById(id);
        Optional<Product> actualProduct = productService.findById(id);

        assertThat(actualProduct).isEqualTo(null);
    }

    @DisplayName("assert that service delete product from cache")
    @Test
    void checkDeleteProductReturnNullFromCache() {
        Optional<Product> expectedProduct = Optional.of(ProductTestBuilder.aProduct()
                .withId(1)
                .build());
        Integer id = expectedProduct.get().getId();

        doReturn(expectedProduct, (Object) null).when(productDao).findById(id);
        productService.findById(id);
        productService.delete(id);
        Optional<Product> actualProduct = productService.findById(id);

        assertThat(actualProduct).isNull();
    }

    @DisplayName("assert that service return saved product from cache")
    @Test
    void checkSaveReturnSavedProduct() {
        Optional<Product> expectedProduct = Optional.of(ProductTestBuilder.aProduct()
                .withId(1)
                .build());
        doReturn(expectedProduct).when(productDao).save(expectedProduct.get());

        Optional<Product> actualProduct = productService.save(expectedProduct.get());

        assertThat(actualProduct).isEqualTo(expectedProduct);
    }

    @DisplayName("assert that service return updated product")
    @Test
    void checkUpdateReturnUpdatedProduct() {
        Optional<Product> product = Optional.of(ProductTestBuilder.aProduct()
                .withId(1)
                .withName("Bacon")
                .build());
        Optional<Product> updatedProduct = Optional.of(ProductTestBuilder.aProduct()
                .withId(1)
                .withName("Egg")
                .build());
        //to get first version of Product from productService
        doReturn(product).when(productDao).save(any());

        productService.save(product.get());
        productService.update(updatedProduct.get());
        Optional<Product> actualProduct = productService.findById(1);

        assertThat(actualProduct).isEqualTo(updatedProduct);
    }
}