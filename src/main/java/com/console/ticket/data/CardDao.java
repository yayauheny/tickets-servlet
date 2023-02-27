package com.console.ticket.data;

import com.console.ticket.entity.Card;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.exception.InputException;
import com.console.ticket.util.ConnectionManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter
@Getter
public class CardDao {
    private static CardDao INSTANCE;
    private static String FIND_CARD_BY_ID = """
            SELECT * FROM company.discount_card WHERE id = ?
            """;

    private static String CARDS_SELECT_ALL = """
            SELECT * FROM company.discount_card
            """;

    public static CardDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CardDao();
        }
        return INSTANCE;
    }

    public Optional<Card> findCardById(Integer id) throws DatabaseException, InputException {
        if (id == null || id < 0) {
            throw new InputException("Error find card by id: " + id);
        }
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(FIND_CARD_BY_ID)) {

            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Card card = null;

            if (resultSet.next()) {
                card = buildCard(resultSet);
            }

            return Optional.ofNullable(card);
        } catch (SQLException e) {
            throw new DatabaseException("Error find card by id: " + id, e);
        }
    }

    private Card buildCard(ResultSet resultSet) throws DatabaseException {
        try {
            return Card.builder()
                    .cardNumber(resultSet.getInt("id"))
                    .discountSize(resultSet.getDouble("discount"))
                    .build();
        } catch (SQLException e) {
            throw new DatabaseException("Error create card: ", e);
        }
    }

    public List<Optional<Card>> findAll() throws DatabaseException {
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
            return cardsList;
        } catch (SQLException e) {
            throw new DatabaseException("Error get all cards from database: " + e.getMessage());
        }
    }
}
