package entity;

//номер карты

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Card {
    private Integer cardNumber;

    private Double discountSize;

//    public Card(Integer cardNumber, Double discountSize) {
//        this.cardNumber = cardNumber;
//        this.discountSize = discountSize;
//    }
//
//    public Card(int cardNumber) {
//        this.cardNumber = cardNumber;
//        this.discountSize = Constants.CARD_DISCOUNT;
//    }
}
