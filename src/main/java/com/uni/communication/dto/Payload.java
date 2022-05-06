package com.uni.communication.dto;

public abstract class Payload {
    private final int lobbyID;
    private final int playerID;

    public Payload(int lobbyID, int playerID) {
        this.lobbyID = lobbyID;
        this.playerID = playerID;
    }

    public int getLobbyID() {
        return lobbyID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
