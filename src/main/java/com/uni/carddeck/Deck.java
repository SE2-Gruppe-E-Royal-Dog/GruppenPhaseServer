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
        LinkedList<Card> list = new LinkedList<>();
        list = addStandardCards(list);
        list = addAdditionalCards(list);
        remainingCards = shuffleDeck(list);
    }

    private LinkedList<Card> addStandardCards(LinkedList<Card> list){
        for(int i=0;i<7;i++){
            list.add(new Card(Cardtype.TWO));
            list.add(new Card(Cardtype.THREE));
            list.add(new Card(Cardtype.FOUR_PLUSMINUS));
            list.add(new Card(Cardtype.FIVE));
            list.add(new Card(Cardtype.SIX));
            list.add(new Card(Cardtype.ONETOSEVEN));
            list.add(new Card(Cardtype.EIGTH));
            list.add(new Card(Cardtype.NINE));
            list.add(new Card(Cardtype.TEN));
            list.add(new Card(Cardtype.ONEORELEVEN_START));
            list.add(new Card(Cardtype.TWELVE));
            list.add(new Card(Cardtype.THIRTEEN_START));
        }
        for(int i=0;i<3;i++){
            list.add(new Card(Cardtype.ONEORELEVEN_START));
            list.add(new Card(Cardtype.THIRTEEN_START));
        }
        return list;
    }

    private LinkedList<Card> addAdditionalCards(LinkedList<Card> list){
        for(int i=0;i<7;i++){
            list.add(new Card(Cardtype.EQUAL));
            list.add(new Card(Cardtype.SWITCH));
            if(i<6){
                list.add(new Card(Cardtype.MAGNET));
            }
        }
        return list;
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
        while(num>=0){
            list.add(drawCard());
            num--;
        }
        return list;
    }

    public List<Card> getDeck(){
        return remainingCards;
    }
}
