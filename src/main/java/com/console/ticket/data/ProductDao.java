package com.console.ticket.data;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class ProductDao {


    private static ProductDao INSTANCE;
    private static String FIND_PRODUCT_BY_ID = """
            SELECT * FROM company.product WHERE id = ?
            """;

    private static String PRODUCTS_SELECT_ALL = """
            SELECT * FROM company.product
            """;

    public Optional<Product> findProductById(Integer id) throws DatabaseException {
        if (id == null || id < 0) {
            throw new InputException("Error find product by id: " + id);
        }
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(FIND_PRODUCT_BY_ID)) {

            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Product product = null;

            if (resultSet.next()) {
                product = buildProduct(resultSet);
            }

            return Optional.ofNullable(product);
        } catch (SQLException e) {
            throw new DatabaseException("Error find product by id: " + id, e);
        }
    }

    private static Product buildProduct(ResultSet resultSet) throws DatabaseException {
        try {
            return Product.builder().id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .price(resultSet.getDouble("price"))
                    .isDiscount(resultSet.getBoolean("discount"))
                    .build();
        } catch (SQLException e) {
            throw new DatabaseException("Error create product: ", e);
        }
    }

    public List<Optional<Product>> findAll() throws DatabaseException {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(PRODUCTS_SELECT_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Optional<Product>> productsList = new ArrayList<>();

            while (resultSet.next()) {
                Optional<Product> product = Optional.ofNullable(Product.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .price(resultSet.getDouble("price"))
                        .isDiscount(resultSet.getBoolean("discount"))
                        .build());
                productsList.add(product);
            }
            return productsList;
        } catch (SQLException e) {
            throw new DatabaseException("Error get all cards from database: " + e.getMessage());
        }
    }

    public static ProductDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProductDao();
        }
        return INSTANCE;
    }
}
