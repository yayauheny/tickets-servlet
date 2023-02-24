package com.console.ticket.data;

import com.console.ticket.entity.Card;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.util.ConnectionManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter
@Getter
public class CardDAO {
    private static CardDAO INSTANCE;
    private static Card card = null;


    private static String FIND_CARD_BY_ID = """
            SELECT * FROM company.discount_card WHERE id = ?
            """;

    public static CardDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CardDAO();
        }
        return INSTANCE;
    }

    public Optional<Card> findCardById(Integer id) throws DatabaseException {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(FIND_CARD_BY_ID)) {

            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
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
}
