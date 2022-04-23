package com.uni.carddeck;

public class Card {
    Cardtype cardtype;

    protected Card(Cardtype cardtype){
        this.cardtype = cardtype;
    }

    public Cardtype getCardtype() {
        return cardtype;
    }
}
