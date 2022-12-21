package data;

import constants.Constants;
import entity.Card;
import entity.product.Product;
import entity.product.ValidateProductBuilder;
import exception.DataBaseException;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private static DataBase instance;
    private Card card;
    private ArrayList<Product> products = new ArrayList<>();

    private DataBase() {

    }

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
                card = new Card(Constants.CASHIER_NUMBER, Constants.STANDARD_DISCOUNT);
            }
        } catch (SQLException e) {
            throw new RuntimeException(new DataBaseException("Exception while finding card from database"));
        }
    }


    private static Product buildProduct(ResultSet resultSet, int quantity) throws SQLException {
        return new ValidateProductBuilder()
                .setId(resultSet.getInt("id"))
                .setName(resultSet.getString("name"))
                .setPrice(resultSet.getDouble("price"))
                .setQuantity(quantity)
                .setDiscount(resultSet.getBoolean("discount"))
                .build();
    }

    private static Card buildCard(ResultSet resultSet) throws SQLException {
        return new Card(resultSet.getInt("id"), resultSet.getDouble("discount"));
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
