package com.console.ticket.data;

import com.console.ticket.entity.Product;
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
@Getter
@Setter
public class ProductDAO {
    private static ProductDAO INSTANCE;
    private static String FIND_PRODUCT_BY_ID = """
            SELECT * FROM company.product WHERE id = ?
            """;

    private static Product product = null;

    public Optional<Product> findProductByIdAndSetQuantity(Integer id, int quantity) throws DatabaseException {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(FIND_PRODUCT_BY_ID)) {

            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                product = buildProduct(resultSet, quantity);
            }

            return Optional.ofNullable(product);
        } catch (SQLException e) {
            throw new DatabaseException("Error find product by id: " + id, e);
        }
    }

    private static Product buildProduct(ResultSet resultSet, int quantity) throws DatabaseException {
        try {
            return Product.builder().id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .price(resultSet.getDouble("price"))
                    .isDiscount(resultSet.getBoolean("discount"))
                    .quantity(quantity)
                    .build();
        } catch (SQLException e) {
            throw new DatabaseException("Error create product: ", e);
        }
    }

    public static ProductDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProductDAO();
        }
        return INSTANCE;
    }
}
