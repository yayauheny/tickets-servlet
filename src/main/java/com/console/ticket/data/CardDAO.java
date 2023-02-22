package com.console.ticket.data;

import com.console.ticket.constants.Constants;
import com.console.ticket.entity.Card;
import com.console.ticket.exception.DataBaseException;
import com.console.ticket.util.ConnectionManager;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Setter
@Getter
public class CardDAO {
    private static Optional<Card> card = Optional.empty();
    public static Optional<Card> findCardById(Integer id) throws DataBaseException {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(Constants.CARD_SELECT)) {

            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return Optional.ofNullable(buildCard(resultSet));
        } catch (SQLException e) {
            throw new DataBaseException("Error find card by id: " + id, e);
        }
    }

    private static Card buildCard(ResultSet resultSet) throws DataBaseException {
        try {
            return Card.builder()
                    .cardNumber(resultSet.getInt("id"))
                    .discountSize(resultSet.getDouble("discount"))
                    .build();
        } catch (SQLException e) {
            throw new DataBaseException("Error create card: ", e);
        }
    }
}
