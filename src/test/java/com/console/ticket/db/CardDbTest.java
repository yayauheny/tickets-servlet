package com.console.ticket.db;

import com.console.ticket.constants.Constants;
import com.console.ticket.data.CardDAO;
import com.console.ticket.entity.Card;
import com.console.ticket.exception.DataBaseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import com.console.ticket.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CardDbTest {
    @ParameterizedTest
    @MethodSource("com.console.ticket.db.CardDbTest#getAllCardsFromDb")
    void canFindExistingCardParameterizedTest(Optional<Card> card, Integer cardNumber) {
        Optional<Card> foundedCard = CardDAO.findCardById(cardNumber);

        assertThat(foundedCard).isPresent();
        assertThat(foundedCard.get()).isEqualTo(card.get());
    }


    static Stream<Arguments> getAllCardsFromDb() throws DataBaseException {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(Constants.CARDS_SELECT_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Optional<Card>> cardsList = new ArrayList<>();

            while (resultSet.next()) {
                Optional<Card> card = Optional.ofNullable(Card.builder()
                        .cardNumber(resultSet.getInt("id"))
                        .discountSize(resultSet.getDouble("discount"))
                        .build());
                cardsList.add(card);
            }

            return cardsList.stream().map(card -> Arguments.of(card, card.get().getCardNumber()));

        } catch (SQLException e) {
            throw new DataBaseException("Error getting all cards from db: " + e.getMessage());
        }
    }

//    void checkExceptionIfCardNotExist(Integer cardNumber) {
//
//    }


}

