package com.console.ticket.entity;

//номер карты

import lombok.*;

@Data
@Builder
public class Card {
    private int id;
    private double discountSize;
}
