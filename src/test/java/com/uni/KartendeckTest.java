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
    public void deckWirdBefuellt(){
        Assertions.assertEquals(110,deck.getDeck().size());
    }

    @Test
    public void deckNeuMischen(){
        for(int i=0;i<110;i++){
            deck.karteZiehen();
        }
        //Deck ist leer
        Assertions.assertEquals(0,deck.getDeck().size());
        deck.karteZiehen();
        Assertions.assertEquals(109,deck.getDeck().size());
    }
}
