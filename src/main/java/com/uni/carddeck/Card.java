package com.uni.carddeck;

public class Card {
    private static int count = 0;
    private final Cardtype cardtype;
    private final int cardID;

    protected Card(Cardtype cardtype){
        this.cardtype = cardtype;
        this.cardID = ++count;
    }

    public Cardtype getCardtype() {
        return cardtype;
    }

    public int getCardID() {
        return cardID;
    }
}
