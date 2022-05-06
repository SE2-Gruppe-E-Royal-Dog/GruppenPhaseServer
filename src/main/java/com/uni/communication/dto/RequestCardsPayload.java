package com.uni.communication.dto;

public class RequestCardsPayload extends Payload{
    boolean sendAll;
    int numOfRequestedCards;

    public RequestCardsPayload(int lobbyID, int playerID, boolean sendAll, int numOfRequestedCards) {
        super(lobbyID, playerID);
        this.sendAll = sendAll;
        this.numOfRequestedCards = numOfRequestedCards;
    }

    public boolean isSendAll() {
        return sendAll;
    }

    public int getNumOfRequestedCards() {
        return numOfRequestedCards;
    }
}
