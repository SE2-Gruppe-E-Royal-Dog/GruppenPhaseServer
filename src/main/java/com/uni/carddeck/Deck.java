package com.uni.carddeck;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Deck {
    private static Random rand = new Random();
    private LinkedList<Card> remainingCards;

    public Deck(){
        remainingCards = new LinkedList<>();
        fillDeck();
    }

    private void fillDeck(){
        LinkedList<Card> list = new LinkedList<>();
        list = addStandardCards(list);
        list = addAdditionalCards(list);
        remainingCards = shuffleDeck(list);
    }

    private LinkedList<Card> addStandardCards(LinkedList<Card> list){
        for(int i=0;i<7;i++){
            list.add(new NumberCard(2));
            list.add(new NumberCard(3));
            list.add(new NumberCard(4));
            list.add(new NumberCard(5));
            list.add(new NumberCard(6));
            list.add(new NumberCard(7));
            list.add(new NumberCard(8));
            list.add(new NumberCard(9));
            list.add(new NumberCard(10));
            list.add(new NumberCard(11));
            list.add(new NumberCard(12));
            list.add(new NumberCard(13));
        }
        for(int i=0;i<3;i++){
            list.add(new NumberCard(11));
            list.add(new NumberCard(13));
        }
        return list;
    }

    private LinkedList<Card> addAdditionalCards(LinkedList<Card> list){
        for(int i=0;i<7;i++){
            list.add(new SpecialCard(Cardtype.EQUAL));
            list.add(new SpecialCard(Cardtype.SWITCH));
            if(i<6){
                list.add(new SpecialCard(Cardtype.MAGNET));
            }
        }
        return list;
    }

    private LinkedList<Card> shuffleDeck(LinkedList<Card> zuMischen) {
        LinkedList<Card> gemischt = new LinkedList<>();
        while (!zuMischen.isEmpty()) {
            int randIndex = rand.nextInt(zuMischen.size());
            Card karte = zuMischen.remove(randIndex);
            gemischt.add(karte);
        }
        return gemischt;
    }

    public Card drawCard(){
        if(remainingCards.isEmpty()){
            fillDeck();
        }
        return remainingCards.removeFirst();
    }

    public List<Card> getDeck(){
        return remainingCards;
    }
}
