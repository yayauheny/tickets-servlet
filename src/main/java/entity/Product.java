package entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Product {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private boolean isDiscount;
}
