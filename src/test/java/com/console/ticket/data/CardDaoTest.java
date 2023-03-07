package com.console.ticket.data;

import com.console.ticket.entity.Card;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.exception.InputException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CardDaoTest {
    private static CardDao cardDao;

    @BeforeAll
    static void initialize() {
        cardDao = CardDao.getInstance();
    }

    @DisplayName("check if empty for not existing cardId")
    @Test
    void checkFindCardByIdShouldBeEmpty() {
        int cardId = 909090;

        assertThat(cardDao.findCardById(cardId)).isEmpty();
    }

    @DisplayName("find all database cards properly")
    @Test
    void checkFindAllCardsFromDatabaseProperly() {
        int databaseCardsQuantity = 9;

        assertThat(cardDao.findAll()).size().isEqualTo(databaseCardsQuantity);
    }

    @DisplayName("exception if id is negative")
    @Test
    void checkFindByIdThrowsInputExceptionIfNegative() {
        assertThrows(InputException.class, () -> cardDao.findCardById(-1));
    }

    @DisplayName("exception if id is null")
    @Test
    void checkFindByIdThrowsInputExceptionIfNull() {
        assertThrows(InputException.class, () -> cardDao.findCardById(null));
    }

    @DisplayName("find all existing cards by id")
    @ParameterizedTest
    @MethodSource("com.console.ticket.data.CardDaoTest#getAllCards")
    void checkFindCardByIdReturnAllCardsFromDatabase(Optional<Card> card, Integer cardId) {
        Optional<Card> foundCard = cardDao.findCardById(cardId);

        assertThat(foundCard.get()).isEqualTo(card.get());
    }

    @DisplayName("arguments of all database cards")
    static Stream<Arguments> getAllCards() throws DatabaseException {
        return cardDao.findAll().stream()
                .map(card -> Arguments.of(card, card.get().getCardNumber()));
    }
}
