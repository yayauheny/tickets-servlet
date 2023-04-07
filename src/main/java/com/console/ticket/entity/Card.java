package com.console.ticket.entity;

//номер карты

import lombok.*;

@Data
@Builder
public class Card {
    private Integer id;
    private Double discountSize;
}
