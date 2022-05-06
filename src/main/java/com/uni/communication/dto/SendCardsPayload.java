package com.uni.communication.dto;

import com.uni.carddeck.Card;

import java.util.LinkedList;

public class SendCardsPayload extends Payload{
    private LinkedList<Card> cards;

    public SendCardsPayload(int lobbyID, int playerID, LinkedList<Card> cards) {
        super(lobbyID, playerID);
        this.cards = cards;
    }
}