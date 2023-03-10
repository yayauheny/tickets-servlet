package com.console.ticket.util;

import com.console.ticket.entity.Card;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCard")
@With
public class CardTestBuilder implements TestBuilder<Card> {
    private Integer id = 0;
    private Double discountSize = 0D;

    @Override
    public Card build() {
        final var card = Card.builder()
                .id(id)
                .discountSize(discountSize)
                .build();
        return card;
    }
}
