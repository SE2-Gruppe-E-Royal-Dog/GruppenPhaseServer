package com.uni.carddeck;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Deck {
    private static final Random rand = new Random();
    private LinkedList<Card> remainingCards;

    public Deck(){
        remainingCards = new LinkedList<>();
        fillDeck();
    }

    private void fillDeck(){
        var list = new LinkedList<Card>();
        list.addAll(getStandardCards());
        list.addAll(getAdditionalCards());
        remainingCards = shuffleDeck(list);
    }

    private List<Card> getStandardCards(){
        var standardCards = new LinkedList<Card>();
        for(int i=0;i<7;i++){
            standardCards.add(new Card(Cardtype.TWO));
            standardCards.add(new Card(Cardtype.THREE));
            standardCards.add(new Card(Cardtype.FOUR_PLUSMINUS));
            standardCards.add(new Card(Cardtype.FIVE));
            standardCards.add(new Card(Cardtype.SIX));
            standardCards.add(new Card(Cardtype.ONETOSEVEN));
            standardCards.add(new Card(Cardtype.EIGTH));
            standardCards.add(new Card(Cardtype.NINE));
            standardCards.add(new Card(Cardtype.TEN));
            standardCards.add(new Card(Cardtype.ONEORELEVEN_START));
            standardCards.add(new Card(Cardtype.TWELVE));
            standardCards.add(new Card(Cardtype.THIRTEEN_START));
        }

        for(int i=0;i<3;i++){
            standardCards.add(new Card(Cardtype.ONEORELEVEN_START));
            standardCards.add(new Card(Cardtype.THIRTEEN_START));
        }
        return standardCards;
    }

    private LinkedList<Card> getAdditionalCards(){
        var additionalCards = new LinkedList<Card>();
        for(int i=0;i<7;i++){
            additionalCards.add(new Card(Cardtype.EQUAL));
            additionalCards.add(new Card(Cardtype.SWITCH));
            if(i<6){
                additionalCards.add(new Card(Cardtype.MAGNET));
            }
        }
        return additionalCards;
    }

    private LinkedList<Card> shuffleDeck(LinkedList<Card> toShuffle) {
        LinkedList<Card> shuffled = new LinkedList<>();
        while (!toShuffle.isEmpty()) {
            int randIndex = rand.nextInt(toShuffle.size());
            Card card = toShuffle.remove(randIndex);
            shuffled.add(card);
        }
        return shuffled;
    }

    public Card drawCard(){
        if(remainingCards.isEmpty()){
            fillDeck();
        }
        return remainingCards.removeFirst();
    }

    public List<Card> drawCards(int num){
        LinkedList<Card> list = new LinkedList<>();
        while(num>0){
            list.add(drawCard());
            num--;
        }
        return list;
    }

    public List<Card> getDeck(){
        return remainingCards;
    }
}
