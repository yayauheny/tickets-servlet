package com.console.ticket.service;

import com.console.ticket.data.CardDao;
import com.console.ticket.data.ProductDao;
import com.console.ticket.entity.Card;
import com.console.ticket.entity.Product;
import com.console.ticket.util.CardTestBuilder;
import com.console.ticket.util.ProductTestBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(
        MockitoExtension.class
)
class ReceiptCalculatorTest {
    @Mock
    private static ProductDao productDao;
    @Mock
    private static CardDao cardDao;
    public static List<Product> productList;

    @BeforeAll
    static void initialize() {
        productDao = ProductDao.getInstance();
        cardDao = CardDao.getInstance();
        productList = List.of(
                ProductTestBuilder.aProduct()
                        .withPrice(15D)
                        .withDiscount(true)
                        .withQuantity(10)
                        .build(),
                ProductTestBuilder.aProduct()
                        .withPrice(5D)
                        .withDiscount(false)
                        .withQuantity(5)
                        .build(),
                ProductTestBuilder.aProduct()
                        .withPrice(10)
                        .withDiscount(false)
                        .withQuantity(5)
                        .build());
    }

    @Nested
    class CalculateDiscountPerProductTest {
        @DisplayName("check sale for discount products")
        @Test
        void checkCalculateDiscountPerProductReturn5() {
            Optional<Product> expectedProduct = Optional.of(ProductTestBuilder.aProduct()
                    .withId(1)
                    .withPrice(5D)
                    .withQuantity(10)
                    .withDiscount(true)
                    .build());

            doReturn(expectedProduct).when(productDao).findById(1);
            Product actualProduct = productDao.findById(1).get();

            double actualDiscount = ReceiptCalculator.calculateDiscountPerProduct(actualProduct);
            double expectedDiscount = 5D;

            assertThat(expectedDiscount).isEqualTo(actualDiscount);
        }

        @DisplayName("check sale for products without discount")
        @Test
        void checkCalculateDiscountPerProductReturn0() {
            Optional<Product> expectedProduct = Optional.of(ProductTestBuilder.aProduct()
                    .withId(1)
                    .withPrice(10D)
                    .withQuantity(3)
                    .withDiscount(false)
                    .build());

            doReturn(expectedProduct).when(productDao).findById(1);
            Product actualProduct = productDao.findById(1).get();

            double actualDiscount = ReceiptCalculator.calculateDiscountPerProduct(actualProduct);
            double expectedDiscount = 0D;

            assertThat(expectedDiscount).isEqualTo(actualDiscount);
        }
    }

    @Nested
    class CalculateDiscountPricePerCardTest {
        @DisplayName("check total price with product and card sales")
        @Test
        void checkCalculateDiscountPricePerCardReturn189() {
            Optional<Card> expectedCard = Optional.of(CardTestBuilder.aCard()
                    .withId(1111)
                    .withDiscountSize(10D)
                    .build());

            Mockito.doReturn(expectedCard).when(cardDao).findById(1);
            Card actualCard = cardDao.findById(1).get();

            double actualDiscountPrice = ReceiptCalculator.calculateDiscountPricePerCard(productList, actualCard);
            double expectedDiscountPrice = 189D;

            assertThat(expectedDiscountPrice).isEqualTo(actualDiscountPrice);
        }

        @DisplayName("check total price without card sale")
        @Test
        void checkCalculateDiscountPricePerCardReturn210() {
            Optional<Card> expectedCard = Optional.of(CardTestBuilder.aCard()
                    .withId(1111)
                    .withDiscountSize(0D)
                    .build());

            Mockito.doReturn(expectedCard).when(cardDao).findById(1);
            Card actualCard = cardDao.findById(1).get();

            double actualDiscountPrice = ReceiptCalculator.calculateDiscountPricePerCard(productList, actualCard);
            double expectedDiscountPrice = 210D;

            assertThat(actualDiscountPrice).isEqualTo(expectedDiscountPrice);
        }
    }

    @Nested
    class CalculateCardDiscountTest {
        @DisplayName("check sum of card discount")
        @Test
        void checkCalculateCardDiscountReturn136_5() {
            Optional<Card> expectedCard = Optional.of(CardTestBuilder.aCard()
                    .withId(2222)
                    .withDiscountSize(35D)
                    .build());

            Mockito.doReturn(expectedCard).when(cardDao).findById(1);
            Card actualCard = cardDao.findById(1).get();

            double actualCardDiscount = ReceiptCalculator.calculateCardDiscount(productList, actualCard);
            double expectedCardDiscount = 73.5D;

            assertThat(expectedCardDiscount).isEqualTo(actualCardDiscount);
        }

        @Test
        void calculateCardDiscount() {
            Optional<Card> expectedCard = Optional.of(CardTestBuilder.aCard()
                    .withId(2222)
                    .withDiscountSize(0D)
                    .build());

            Mockito.doReturn(expectedCard).when(cardDao).findById(1);
            Card actualCard = cardDao.findById(1).get();

            double actualCardDiscount = ReceiptCalculator.calculateCardDiscount(productList, actualCard);
            double expectedCardDiscount = 0D;

            assertThat(expectedCardDiscount).isEqualTo(actualCardDiscount);
        }
    }
}
