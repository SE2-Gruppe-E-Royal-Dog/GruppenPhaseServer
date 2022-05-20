package com.uni.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setup(){
        player = new Player("1");
    }

    @AfterEach
    public void cleanup(){
        player = null;
    }

    @Test
    public void reduceNumOfCardsLeftTest(){
        player.reduceCardsLeft();

        Assertions.assertEquals(-1,player.getNumOfCardsLeft());
    }

    @Test
    public void setNumOfCardsLeftTest(){
        player.setNumOfCardsLeft(10);

        Assertions.assertEquals(10, player.getNumOfCardsLeft());
    }
}
