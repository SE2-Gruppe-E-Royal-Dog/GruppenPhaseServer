package com.uni.kartendeck;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
        Random rand = new Random();
        while (!zuMischen.isEmpty()) {
            int randIndex = rand.nextInt(zuMischen.size());
            Karte karte = zuMischen.remove(randIndex);
            gemischt.add(karte);
        }
        return gemischt;
    }

    public Karte karteZiehen(){
        if(restlicheKarten.isEmpty()){
            leeresDeckbefuellen();
        }
        return restlicheKarten.removeFirst();
    }

    public List<Karte> getDeck(){
        return restlicheKarten;
    }
}
