package com.uni.game;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Player {
    private final String id;
    private final String name;

    public Player(String name) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
    }
}
