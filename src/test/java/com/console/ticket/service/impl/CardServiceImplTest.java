package com.console.ticket.service.impl;

import com.console.ticket.data.CardDao;
import com.console.ticket.entity.Card;
import com.console.ticket.util.CardTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {
    @InjectMocks
    private CardServiceImpl cardService;
    @Mock
    private CardDao cardDao;

    @DisplayName("assert that service return cached value from cache")
    @Test
    void checkGetByIdReturnCardFromCache() {
        Optional<Card> expectedCard = Optional.of(CardTestBuilder.aCard()
                .withId(1)
                .build());
        Integer id = expectedCard.get().getId();

        doReturn(expectedCard, null, null).when(cardDao).findById(id);

        cardService.findById(id);
        cardService.findById(id);
        Optional<Card> actualCard = cardService.findById(id);

        assertThat(actualCard).isEqualTo(expectedCard);
    }

    @DisplayName("assert that service return null when value not exists")
    @Test
    void checkGetByIdReturnNull() {
        Optional<Card> expectedCard = Optional.of(CardTestBuilder.aCard()
                .withId(1)
                .build());
        Integer id = expectedCard.get().getId();
        doReturn(null).when(cardDao).findById(id);
        Optional<Card> actualCard = cardService.findById(id);

        assertThat(actualCard).isEqualTo(null);
    }

    @DisplayName("assert that service delete element from cache")
    @Test
    void checkDeleteCardReturnNullFromCache() {
        Optional<Card> expectedCard = Optional.of(CardTestBuilder.aCard()
                .withId(1)
                .build());
        Integer id = expectedCard.get().getId();

        doReturn(expectedCard, (Object) null).when(cardDao).findById(id);
        cardService.findById(id);
        cardService.delete(id);
        Optional<Card> actualCard = cardService.findById(id);

        assertThat(actualCard).isNull();
    }

    @DisplayName("assert that service return saved element from cache")
    @Test
    void checkSaveReturnSavedCard() {
        Optional<Card> expectedCard = Optional.of(CardTestBuilder.aCard()
                .withId(1)
                .build());
        doReturn(expectedCard).when(cardDao).save(expectedCard.get());

        Optional<Card> actualCard = cardService.save(expectedCard.get());

        assertThat(actualCard).isEqualTo(expectedCard);
    }

    @DisplayName("assert that service return updated element")
    @Test
    void checkUpdateReturnUpdatedCard() {
        Optional<Card> card = Optional.of(CardTestBuilder.aCard()
                .withId(1)
                .withDiscountSize(1D)
                .build());
        Optional<Card> updatedCard = Optional.of(CardTestBuilder.aCard()
                .withId(1)
                .withDiscountSize(10D)
                .build());
        //to get first version of card from cardService
        doReturn(card).when(cardDao).save(any());

        cardService.save(card.get());
        cardService.update(updatedCard.get());
        Optional<Card> actualCard = cardService.findById(1);

        assertThat(actualCard).isEqualTo(updatedCard);
    }
}