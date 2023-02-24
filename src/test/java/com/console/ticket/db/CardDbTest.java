package com.console.ticket.db;

import com.console.ticket.data.CardDAO;
import com.console.ticket.entity.Card;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.util.ConnectionManager;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CardDbTest {
    private static String CARDS_SELECT_ALL = """
            SELECT * FROM company.discount_card
            """;
    @ParameterizedTest
    @MethodSource("com.console.ticket.db.CardDbTest#getAllCardsFromDb")
    void canFindExistingCardParameterizedTest(Optional<Card> card, Integer cardNumber) {
        Optional<Card> foundedCard = CardDAO.getInstance().findCardById(cardNumber);

        assertThat(foundedCard).isPresent();
        assertThat(foundedCard.get()).isEqualTo(card.get());
    }


    static Stream<Arguments> getAllCardsFromDb() throws DatabaseException {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(CARDS_SELECT_ALL);
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
            throw new DatabaseException("Error get all cards from database: " + e.getMessage());
        }
    }
}

