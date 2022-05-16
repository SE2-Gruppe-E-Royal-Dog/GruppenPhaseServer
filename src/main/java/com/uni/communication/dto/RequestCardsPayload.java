package com.uni.communication.dto;

public class RequestCardsPayload{
    private final String lobbyID;
    private final String playerID;
    boolean sendAll;
    int numOfRequestedCards;

    public RequestCardsPayload(String lobbyID, String playerID, boolean sendAll, int numOfRequestedCards) {
        this.lobbyID = lobbyID;
        this.playerID = playerID;
        this.sendAll = sendAll;
        this.numOfRequestedCards = numOfRequestedCards;
    }

    public boolean isSendAll() {
        return sendAll;
    }

    public int getNumOfRequestedCards() {
        return numOfRequestedCards;
    }

    public String getLobbyID() {
        return lobbyID;
    }

    public String getPlayerID() {
        return playerID;
    }
}
