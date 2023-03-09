package com.console.ticket.data;

import com.console.ticket.entity.Card;
import com.console.ticket.entity.Product;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.exception.InputException;
import com.console.ticket.util.ConnectionManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter
@Getter
public class CardDao {
    private static CardDao INSTANCE;
    private static String CARD_FIND = """
            SELECT * FROM company.discount_card WHERE id = ?
            """;

    private static String CARD_FIND_ALL = """
            SELECT * FROM company.discount_card
            """;
    private static String CARD_DELETE = """
            DELETE FROM company.discount_card
            WHERE id = ?
            """;
    private static String CARD_SAVE = """
            INSERT INTO company.discount_card (discount)
            VALUES (?);
            """;
    private static String CARD_UPDATE = """
            UPDATE company.discount_card
            SET discount = ?
            WHERE id = ?
            """;

    public static CardDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CardDao();
        }
        return INSTANCE;
    }

    public Optional<Card> findById(Integer id) throws DatabaseException, InputException {
        if (id == null || id < 0) {
            throw new InputException("Error find card by id: " + id);
        }
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(CARD_FIND)) {

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

    public void delete(Integer id) throws DatabaseException, InputException {
        if (id == null || id < 0) {
            throw new InputException("Error find card by id: " + id);
        }

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(CARD_DELETE)) {
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error delete card by id: " + id, e);
        }
    }

    public Card save(Card card) throws DatabaseException {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(CARD_SAVE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDouble(1, card.getDiscountSize());

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                card.setCardNumber(keys.getInt("id"));
            }

            return card;
        } catch (SQLException e) {
            throw new DatabaseException("Error save card: " + card.getCardNumber(), e);
        }
    }

    public void update(Card card) throws DatabaseException {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(CARD_UPDATE)) {
            preparedStatement.setDouble(1, card.getDiscountSize());
            preparedStatement.setInt(2, card.getCardNumber());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error update card: " + card.getCardNumber(), e);
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
             var preparedStatement = connection.prepareStatement(CARD_FIND_ALL);
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
