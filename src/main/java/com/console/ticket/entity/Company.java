package com.console.ticket.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Company {
    private String name;
    private String address;
    private String currency;
    private int salePercent;

    public Company(String name, String address, int salePercent, String currency) {
        this.name = name;
        this.address = address;
        this.salePercent = salePercent;
        this.currency = currency;
    }
}
