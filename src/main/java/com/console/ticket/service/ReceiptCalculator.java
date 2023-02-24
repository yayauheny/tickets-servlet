package com.console.ticket.service;

import com.console.ticket.constants.Constants;
import com.console.ticket.entity.Card;
import com.console.ticket.entity.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReceiptCalculator {
    public static double calculateDiscountPerProduct(Product product) {
        if (product.isDiscount() && product.getQuantity() > Constants.DISCOUNT_AFTER) {
            return product.getPrice() * product.getQuantity() * Constants.PRODUCT_DISCOUNT / 100;
        } else return 0;
    }

    public static double calculateDiscountPricePerCard(List<Product> productList, Card card) {
        double totalPrice = calculateTotalPriceForProducts(productList);
        return (totalPrice - (totalPrice * card.getDiscountSize() / 100));
    }

    public static double calculateCardDiscount(List<Product> productList, Card card) {
        double totalPrice = calculateTotalPriceForProducts(productList);
        return (totalPrice * card.getDiscountSize() / 100);
    }

    private static double calculateDiscountPriceOrReturnTotalPrice(Product product) {
        double productPrice = product.getPrice() * product.getQuantity();

        if (product.isDiscount() && product.getQuantity() > Constants.DISCOUNT_AFTER) {
            double productSale = productPrice * Constants.PRODUCT_DISCOUNT / 100;
            return (productPrice - productSale);
        } else return productPrice;
    }

    private static double calculateTotalPriceForProducts(List<Product> productList) {
        return productList.stream()
                .mapToDouble(ReceiptCalculator::calculateDiscountPriceOrReturnTotalPrice)
                .sum();
    }
}
