package com.uni.communication.dto;

import com.uni.carddeck.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Setter
@Getter

public class SendCardsPayload{
    private final List<Card> cards;

    public SendCardsPayload(List<Card> cards) {
        this.cards = cards;
    }
}