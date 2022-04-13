package com.uni;

import com.uni.Kartendeck.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class KartendeckTest {

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
    public void deck_wird_befuellt(){
        Assertions.assertEquals(110,deck.getDeck().size());
    }

    @Test
    public void deck_neu_mischen(){
        for(int i=0;i<110;i++){
            deck.karte_ziehen();
        }
        //Deck ist leer
        Assertions.assertEquals(0,deck.getDeck().size());
        deck.karte_ziehen();
        Assertions.assertEquals(109,deck.getDeck().size());
    }
}
