package com.console.ticket.entity;

//номер карты

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Card {
    private Integer cardNumber;
    private Double discountSize;

}
