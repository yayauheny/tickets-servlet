package com.console.ticket.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Currency {
    BELARUS("BYN"),
    USA("$"),
    FRANCE("€");

    private final String currency;
}
