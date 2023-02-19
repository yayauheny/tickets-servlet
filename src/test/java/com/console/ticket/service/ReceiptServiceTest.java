package com.console.ticket.service;

import constants.Constants;
import data.DataBase;
import entity.Card;
import entity.Company;
import entity.Product;
import entity.product.ValidateProductBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import service.ReceiptService;
import util.Currencies;

import static java.lang.Math.round;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReceiptServiceTest {
    private static final DataBase dataBase = DataBase.getInstance();

    private static final Company company = new Company("Louis Vuitton", "101, avenue des Champs, 75008 Paris",
            10, Currencies.USA.getCurrency());

    private static final Product fish = new ValidateProductBuilder().setId(3).setQuantity(10).build();
    private static final Card maxDiscountCard = new Card(4444);

    @Test
    @Disabled
    void checkDiscount() {
        dataBase.findCardById(maxDiscountCard.getCardNumber());
        dataBase.findProductById(fish.getId(), fish.getQuantity());

        ReceiptService.buildReceipt(company, dataBase);


        assertEquals(54, round(Constants.TOTAL_SUM));
        assertEquals(2, round(Constants.CARD_DISCOUNT));
        assertEquals(3.3, dataBase.getCard().getDiscountSize());
    }

    @AfterAll
    @Disabled
    static void clearList() {
        dataBase.getProductsList().clear();
    }
}
