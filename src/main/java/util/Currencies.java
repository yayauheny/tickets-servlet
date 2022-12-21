package util;

/**
 * Валюты
 *
 */

public enum Currencies {
    BELARUS("BYN"),
    USA("$"),
    FRANCE("€");

    private String currency;

    Currencies(String currency) {
        this.currency = currency;
    }


    public String getCurrency() {
        return currency;
    }
}
