package com.uni.communication.dto;

public abstract class Payload {
    private final String lobbyID;
    private final String playerID;

    public Payload(String lobbyID, String playerID) {
        this.lobbyID = lobbyID;
        this.playerID = playerID;
    }

    public String getLobbyID() {
        return lobbyID;
    }

    public String getPlayerID() {
        return playerID;
    }
}
