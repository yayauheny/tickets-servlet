package com.console.ticket.util;

import com.console.ticket.entity.Card;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCard")
@With
public class CardTestBuilder implements TestBuilder<Card> {
    private Integer cardNumber = 0;
    private Double discountSize = 0D;

    @Override
    public Card build() {
        final var card = Card.builder()
                .cardNumber(cardNumber)
                .discountSize(discountSize)
                .build();
        return card;
    }
}
