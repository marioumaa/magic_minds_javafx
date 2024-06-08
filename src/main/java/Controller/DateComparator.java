package Controller;

import Entity.Evenement;

import java.util.Comparator;
public class DateComparator implements Comparator<Evenement> {
    @Override
    public int compare(Evenement event1, Evenement event2) {
        // Compare les dates de début des événements
        return event1.getDate_debut().compareTo(event2.getDate_debut());
    }
}
