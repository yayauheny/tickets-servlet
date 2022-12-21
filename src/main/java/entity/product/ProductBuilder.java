package entity.product;

public interface ProductBuilder {
    ProductBuilder setId(int id);

    ProductBuilder setName(String name);

    ProductBuilder setQuantity(int quantity);

    ProductBuilder setDiscount(boolean discount);

    ProductBuilder setPrice(double price);

    Product build();

}
