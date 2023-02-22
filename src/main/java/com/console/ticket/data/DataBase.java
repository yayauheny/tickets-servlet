package com.console.ticket.data;

import com.console.ticket.constants.Constants;
import com.console.ticket.entity.Card;
import com.console.ticket.entity.Product;
import com.console.ticket.exception.DataBaseException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.console.ticket.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataBase {
    private static DataBase instance;
    private Card card;
    private ArrayList<Product> products = new ArrayList<>();

    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    public void createTables() {
        try (var connection = ConnectionManager.open();
             var statement = connection.createStatement()) {
            statement.execute(Constants.CREATE_TABLES);
        } catch (SQLException e) {
            throw new RuntimeException(new DataBaseException("Exception while creating database"));
        }
    }

    public void findProductById(Integer id, Integer quantity) {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(Constants.PRODUCTS_SELECT)) {

            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                products.add(buildProduct(resultSet, quantity));
            }
        } catch (SQLException e) {
            throw new RuntimeException(new DataBaseException("Exception while finding products from database"));
        }
    }

    public void findCardById(Integer id) {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(Constants.CARD_SELECT)) {

            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                card = buildCard(resultSet);
            } else {
                card = Card.builder()
                        .cardNumber(Constants.CASHIER_NUMBER)
                        .discountSize(Constants.STANDARD_DISCOUNT)
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(new DataBaseException("Exception while finding card from database"));
        }
    }

    private static Product buildProduct(ResultSet resultSet, int quantity) throws SQLException {
        try {
            return Product.builder().id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .price(resultSet.getDouble("price"))
                    .quantity(quantity)
                    .isDiscount(resultSet.getBoolean("discount"))
                    .build();
        } catch (DataBaseException e) {
            System.out.println("Exception building product: " + e.getMessage());
        }
        return null;
    }

    private static Card buildCard(ResultSet resultSet) throws SQLException {
        try {
            return Card.builder()
                    .cardNumber(resultSet.getInt("id"))
                    .discountSize((resultSet.getDouble("discount")))
                    .build();
        } catch (DataBaseException e) {
            System.out.println("Exception building card: " + e.getMessage());
        }
        return null;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public ArrayList<Product> getProductsList() {
        return products;
    }
}
