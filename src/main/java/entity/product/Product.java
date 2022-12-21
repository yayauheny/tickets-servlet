package entity.product;

/**
 * аукционные товары
 * id, name, quantity
 */


public class Product {
    private int id;
    private String name;

    private int quantity;
    private double price;

    private boolean isDiscount;

    public Product() {
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isDiscount() {
        return isDiscount;
    }

    public double getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDiscount(boolean isDiscount) {
        this.isDiscount = isDiscount;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
