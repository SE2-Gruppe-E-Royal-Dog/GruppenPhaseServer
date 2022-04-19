package com.uni.carddeck;

public class SpecialCard extends Card {
    private Cardtype cardtype;

    public SpecialCard(Cardtype cardtype){
        this.cardtype = cardtype;
    }

    @Override
    public void activate(){
        //Tells where to move
    }
}
