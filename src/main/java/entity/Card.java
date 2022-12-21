package entity;

//номер карты

import constants.Constants;


public class Card {
    private Integer cardNumber;

    private Double discountSize;

    public Card(Integer cardNumber, Double discountSize) {
        this.cardNumber = cardNumber;
        this.discountSize = discountSize;
    }

    public Card(int cardNumber) {
        this.cardNumber = cardNumber;
        this.discountSize = Constants.CARD_DISCOUNT;
    }

    public double getDiscountSize() {
        return discountSize;
    }

    public void setDiscountSize(Double discountSize) {
        this.discountSize = discountSize;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Integer cardNumber) {
        this.cardNumber = cardNumber;
    }

}
