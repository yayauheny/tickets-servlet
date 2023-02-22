package com.console.ticket.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Product {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private boolean isDiscount;
}
