package com.console.ticket.data;

import com.console.ticket.constants.Constants;
import com.console.ticket.entity.Product;
import com.console.ticket.exception.DataBaseException;
import com.console.ticket.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ProductDAO {

    private static Optional<Product> product = Optional.empty();

    public static Optional<Product> findProductById(Integer id, Integer quantity) throws DataBaseException {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(Constants.PRODUCTS_SELECT)) {

            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return product = buildProduct(resultSet, quantity);
        } catch (SQLException e) {
            throw new DataBaseException("Error find product by id: " + id, e);
        }
    }

    private static Optional<Product> buildProduct(ResultSet resultSet, int quantity) throws DataBaseException {
        try {
            return Optional.ofNullable(Product.builder().id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .price(resultSet.getDouble("price"))
                    .isDiscount(resultSet.getBoolean("discount"))
                    .quantity(quantity)
                    .build());
        } catch (SQLException e) {
            throw new DataBaseException("Error create product: ", e);
        }
    }
}
