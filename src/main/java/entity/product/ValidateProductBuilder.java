package entity.product;


import exception.DataBaseException;

/**
 * Builder - pattern для класса Product
 */

public class ValidateProductBuilder implements ProductBuilder {

    Product product = new Product();

    @Override
    public ProductBuilder setId(int id) {
        product.setId(id);
        return this;
    }

    @Override
    public ProductBuilder setName(String name) {
        product.setName(name);
        return this;
    }

    @Override
    public ProductBuilder setQuantity(int quantity) {
        product.setQuantity(quantity);
        return this;
    }

    @Override
    public ProductBuilder setDiscount(boolean isDiscount) {
        product.setDiscount(isDiscount);
        return this;
    }

    @Override
    public ProductBuilder setPrice(double price) {
        product.setPrice(price);
        return this;
    }

    @Override
    public Product build() {
        if (product.getId() < 0) {
            throw new RuntimeException(new DataBaseException("Id of product is invalid"));
        }
        if (product.getQuantity() < 0) {
            throw new RuntimeException(new DataBaseException("Quantity of product is invalid"));
        }
        return product;
    }
}
