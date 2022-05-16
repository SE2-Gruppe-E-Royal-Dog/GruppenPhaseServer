package com.uni.communication.dto;

import com.uni.carddeck.Card;

import java.util.LinkedList;

public class SendCardsPayload{
    private final LinkedList<Card> cards;

    public SendCardsPayload(LinkedList<Card> cards) {
        this.cards = cards;
    }
}