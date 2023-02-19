package com.console.ticket.db;

import data.DataBase;
import entity.Card;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CardDbTest {
    private DataBase dataBase = DataBase.getInstance();
    private static ArrayList<Card> cards = new ArrayList<>();
    private static ArrayList<Card> cardsFound = new ArrayList<>();

    @Test
    @Disabled
    void findCard() {
        {
            cards.add(new Card(1111, 0.1));
            cards.add(new Card(2222, 0.2));
            cards.add(new Card(3333, 0.3));
            cards.add(new Card(4444, 4.0));
            cards.add(new Card(9090));
            cards.add(new Card(8080));
        }
        for (int i = 0; i < cards.size(); i++) {
            dataBase.findCardById(cards.get(i).getCardNumber());
            cardsFound.add(dataBase.getCard());
        }

        assertEquals(cardsFound.get(0).getCardNumber(), cards.get(0).getCardNumber());
        assertEquals(cardsFound.get(1).getCardNumber(), cards.get(1).getCardNumber());
        assertEquals(cardsFound.get(2).getCardNumber(), cards.get(2).getCardNumber());
        assertEquals(cardsFound.get(3).getCardNumber(), cards.get(3).getCardNumber());

        assertNotEquals(cardsFound.get(4).getCardNumber(), cards.get(4).getCardNumber());
        assertNotEquals(cardsFound.get(5).getCardNumber(), cards.get(5).getCardNumber());

//    }
    }

    @AfterAll
    @Disabled
    static void clear() {
        cards = null;
        cardsFound = null;
    }
}

