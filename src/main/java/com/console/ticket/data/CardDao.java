package com.console.ticket.data;

import com.console.ticket.entity.Card;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.exception.InputException;
import com.console.ticket.util.ConnectionManager;
import com.console.ticket.util.SqlRequestsUtil;
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
public class CardDao implements DaoTemplate<Card> {
    private static CardDao INSTANCE = new CardDao();

    @Override
    public Optional<Card> findById(Integer id) throws DatabaseException, InputException {
        if (id == null || id < 0) {
            throw new InputException("Error find card by id: " + id);
        }
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(SqlRequestsUtil.CARD_FIND)) {
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

    @Override
    public void delete(Integer id) throws DatabaseException {
        if (id == null || id < 0) {
            throw new InputException("Incorrect id format: " + id);
        }

        boolean isDeleted;

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(SqlRequestsUtil.CARD_DELETE)) {
            preparedStatement.setObject(1, id);

            isDeleted = (preparedStatement.executeUpdate() > 0);
        } catch (SQLException e) {
            throw new DatabaseException("Error delete card by id: " + id, e);
        }

        if (!isDeleted) {
            throw new DatabaseException("No card found by id: " + id);
        }
    }

    @Override
    public Optional<Card> save(Card card) throws DatabaseException {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(SqlRequestsUtil.CARD_SAVE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDouble(1, card.getDiscountSize());

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();

            if (keys.next()) {
                card.setId(keys.getInt("id"));
            }

            return Optional.of(card);
        } catch (SQLException e) {
            throw new DatabaseException("Error save card: " + card.getId(), e);
        }
    }

    @Override
    public void update(Card card) throws DatabaseException {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(SqlRequestsUtil.CARD_UPDATE)) {
            preparedStatement.setDouble(1, card.getDiscountSize());
            preparedStatement.setInt(2, card.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error update card: " + card.getId(), e);
        }
    }

    private Card buildCard(ResultSet resultSet) throws DatabaseException {
        try {
            return Card.builder()
                    .id(resultSet.getInt("id"))
                    .discountSize(resultSet.getDouble("discount"))
                    .build();
        } catch (SQLException e) {
            throw new DatabaseException("Error create card: ", e);
        }
    }

    @Override
    public List<Optional<Card>> findAll() throws DatabaseException {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(SqlRequestsUtil.CARD_FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Optional<Card>> cardsList = new ArrayList<>();

            while (resultSet.next()) {
                Optional<Card> card = Optional.ofNullable(buildCard(resultSet));
                cardsList.add(card);
            }
            return cardsList;
        } catch (SQLException e) {
            throw new DatabaseException("Error get all cards from database: " + e.getMessage());
        }
    }

    public static CardDao getInstance() {
        return INSTANCE;
    }
}
