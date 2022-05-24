package com.uni.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTest {

    Player player;

    @BeforeEach
    void setup(){
        player = new Player("1");
    }

    @AfterEach
    void cleanup(){
        player = null;
    }

    @Test
    void reduceNumOfCardsLeftTest(){
        player.reduceCardsLeft();

        Assertions.assertEquals(-1,player.getNumOfCardsLeft());
    }

    @Test
    void setNumOfCardsLeftTest(){
        player.setNumOfCardsLeft(10);

        Assertions.assertEquals(10, player.getNumOfCardsLeft());
    }
}
