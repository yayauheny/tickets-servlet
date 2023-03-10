package com.console.ticket.util;

import com.console.ticket.entity.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aProduct")
@With
public class ProductTestBuilder implements TestBuilder<Product> {
    private int id = 0;
    private String name = "";
    private int quantity = 0;
    private double price = 0D;
    private boolean isDiscount = false;

    @Override
    public Product build() {
        final var product = Product.builder()
                .id(id)
                .name(name)
                .quantity(quantity)
                .price(price)
                .isDiscount(isDiscount)
                .build();
        return product;
    }
}
