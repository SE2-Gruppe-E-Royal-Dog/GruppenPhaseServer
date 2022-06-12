package com.uni.communication.dto;

import com.uni.carddeck.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

@Setter
@Getter

public class SendCardsPayload{
    private final LinkedList<Card> cards;

    public SendCardsPayload(LinkedList<Card> cards) {
        this.cards = cards;
    }
}