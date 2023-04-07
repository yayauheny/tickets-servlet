package com.console.ticket.entity;

import lombok.*;

@Data
@Builder
public class Product {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private boolean isDiscount;
}
