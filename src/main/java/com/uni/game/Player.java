package com.uni.game;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Player {
    private final String playerId;
    private final String name;

    public Player(String name) {
        this.name = name;
        this.playerId = UUID.randomUUID().toString();
    }
}
