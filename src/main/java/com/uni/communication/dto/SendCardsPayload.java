package com.uni.communication.dto;

import com.uni.carddeck.Card;

import java.util.LinkedList;

public class SendCardsPayload extends Payload{
    private LinkedList<Card> cards;

    public SendCardsPayload(String lobbyID, String playerID, LinkedList<Card> cards) {
        super(lobbyID, playerID);
        this.cards = cards;
    }
}