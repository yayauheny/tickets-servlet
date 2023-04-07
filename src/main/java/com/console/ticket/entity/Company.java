package com.console.ticket.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Company {
    private String name;
    private String address;
    private String currency;

    public Company(String name, String address, String currency) {
        this.name = name;
        this.address = address;
        this.currency = currency;
    }
}
