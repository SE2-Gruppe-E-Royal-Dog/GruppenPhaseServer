package com.uni.Kartendeck;

import java.util.LinkedList;

public class Deck {
    private LinkedList<Karte> restliche_Karten;

    public Deck(){
        restliche_Karten = new LinkedList<>();
        leeres_Deck_befüllen();
    }

    public void leeres_Deck_befüllen(){
        LinkedList<Karte> list = new LinkedList<>();
        standardkarten_hinzufügen(list);
        //zusatzkarten_hinzufügen(list);
    }

    private void standardkarten_hinzufügen(LinkedList<Karte> list){
        for(int i=0;i<7;i++){
            list.add(new Zahlenkarte(2));
            list.add(new Zahlenkarte(3));
            list.add(new Zahlenkarte(4));
            list.add(new Zahlenkarte(5));
            list.add(new Zahlenkarte(6));
            list.add(new Zahlenkarte(7));
            list.add(new Zahlenkarte(8));
            list.add(new Zahlenkarte(9));
            list.add(new Zahlenkarte(10));
            list.add(new Zahlenkarte(11));
            list.add(new Zahlenkarte(12));
            list.add(new Zahlenkarte(13));
        }
        for(int i=0;i<3;i++){
            list.add(new Zahlenkarte(11));
            list.add(new Zahlenkarte(13));
        }
    }

    private void zusatzkarten_hinzufügen(LinkedList<Karte> list){
        for(int i=0;i<7;i++){
            list.add(new Spezialkarte(Kartentyp.gleich));
            list.add(new Spezialkarte(Kartentyp.tausch));
            if(i<6){
                list.add(new Spezialkarte(Kartentyp.magnet));
            }
        }
    }

    private void deck_mischen(LinkedList<Karte> zuMischen){

    }
}
