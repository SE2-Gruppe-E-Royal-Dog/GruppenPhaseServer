package com.uni;

import com.uni.carddeck.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class CardDeckTest {

    private Deck deck;

    @BeforeEach
    public void setup(){
        deck = new Deck();
    }

    @AfterEach
    public void cleanup(){
        deck = null;
    }

    @Test
    public void deckIsFilled(){
        Assertions.assertEquals(110,deck.getDeck().size());
    }

    @Test
    public void deckRefillWhenEmpty(){
        for(int i=0;i<110;i++){
            deck.drawCard();
        }
        //Deck ist leer
        Assertions.assertEquals(0,deck.getDeck().size());
        deck.drawCard();
        Assertions.assertEquals(109,deck.getDeck().size());
    }

    @Test
    public void drawCardsTest(){
        LinkedList<Card> res = (LinkedList<Card>) deck.drawCards(5);

        Assertions.assertEquals(5, res.size());
    }
}
