package com.console.ticket.entity;

import lombok.Getter;

/**
 * Валюты
 *
 */
@Getter

public enum Currency {
    BELARUS("BYN"),
    USA("$"),
    FRANCE("€");

    private String currency;

    Currency(String currency) {
        this.currency = currency;
    }

//    public String getCurrency() {
//        return currency;
//    }
}
