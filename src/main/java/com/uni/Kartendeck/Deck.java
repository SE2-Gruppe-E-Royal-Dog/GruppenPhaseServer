package com.uni.Kartendeck;

import java.util.LinkedList;

public class Deck {
    private LinkedList<Karte> restlicheKarten;

    public Deck(){
        restlicheKarten = new LinkedList<>();
        leeresDeckbefuellen();
    }

    private void leeresDeckbefuellen(){
        LinkedList<Karte> list = new LinkedList<>();
        list = standardkartenHinzufuegen(list);
        list = zusatzkartenHinzufuegen(list);
        restlicheKarten = deckMischen(list);
    }

    private LinkedList<Karte> standardkartenHinzufuegen(LinkedList<Karte> list){
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
        return list;
    }

    private LinkedList<Karte> zusatzkartenHinzufuegen(LinkedList<Karte> list){
        for(int i=0;i<7;i++){
            list.add(new Spezialkarte(Kartentyp.gleich));
            list.add(new Spezialkarte(Kartentyp.tausch));
            if(i<6){
                list.add(new Spezialkarte(Kartentyp.magnet));
            }
        }
        return list;
    }

    private LinkedList<Karte> deckMischen(LinkedList<Karte> zuMischen) {
        LinkedList<Karte> gemischt = new LinkedList<>();
        while (zuMischen.size() > 0) {
            int rand_index = (int) (Math.random() * zuMischen.size());
            Karte karte = zuMischen.remove(rand_index);
            gemischt.add(karte);
        }
        return gemischt;
    }

    public Karte karteZiehen(){
        if(restlicheKarten.size()==0){
            leeresDeckbefuellen();
        }
        return restlicheKarten.removeFirst();
    }

    public LinkedList<Karte> getDeck(){
        return restlicheKarten;
    }
}
