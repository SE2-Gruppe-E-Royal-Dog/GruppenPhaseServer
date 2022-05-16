package com.uni.carddeck;

public class Card {
    private final Cardtype cardtype;

    protected Card(Cardtype cardtype){
        this.cardtype = cardtype;
    }

    public Cardtype getCardtype() {
        return cardtype;
    }
}
