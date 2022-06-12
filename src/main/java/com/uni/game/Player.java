package com.uni.game;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Player {
    private final String id;
    private final String name;
    private int numOfCardsLeft;

    public Player(String name) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
        numOfCardsLeft = 0;
    }

    public void setNumOfCardsLeft(int numOfCardsLeft) {
        this.numOfCardsLeft = numOfCardsLeft;
    }

    public void reduceCardsLeft(){
        numOfCardsLeft--;
    }

    public String getNameOfPlayer(){
        return this.name;
    }
}
