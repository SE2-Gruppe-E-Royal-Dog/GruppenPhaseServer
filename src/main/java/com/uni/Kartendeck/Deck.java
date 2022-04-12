package com.uni.Kartendeck;

import java.util.LinkedList;

public class Deck {
    private LinkedList<Karte> restliche_Karten;

    public Deck(){
        restliche_Karten = new LinkedList<>();
        leeres_Deck_befuellen();
    }

    private void leeres_Deck_befuellen(){
        LinkedList<Karte> list = new LinkedList<>();
        list = standardkarten_hinzufuegen(list);
        list = zusatzkarten_hinzufuegen(list);
        deck_mischen(list);
    }

    private LinkedList<Karte> standardkarten_hinzufuegen(LinkedList<Karte> list){
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

    private LinkedList<Karte> zusatzkarten_hinzufuegen(LinkedList<Karte> list){
        for(int i=0;i<7;i++){
            list.add(new Spezialkarte(Kartentyp.gleich));
            list.add(new Spezialkarte(Kartentyp.tausch));
            if(i<6){
                list.add(new Spezialkarte(Kartentyp.magnet));
            }
        }
        return list;
    }

    private void deck_mischen(LinkedList<Karte> zuMischen) {
        //fügt die Karten in beliebiger Reihenfolge in "restliche_Karten" ein
        while (zuMischen.size() > 0) {
            int rand_index = (int) (Math.random() * zuMischen.size());
            Karte karte = zuMischen.remove(rand_index);
            restliche_Karten.add(karte);
        }
    }

    public Karte karte_ziehen(){
        if(restliche_Karten.size()==0){
            leeres_Deck_befuellen();
        }
        return restliche_Karten.removeFirst();
    }
}
