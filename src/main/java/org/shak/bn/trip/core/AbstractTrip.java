package org.shak.bn.trip.core;

public abstract class AbstractTrip {
    int nbPerson;
    TripDate dates;
    String destination;

    AbstractTrip(int nbPerson, TripDate dates, String destination) {
        this.nbPerson = nbPerson;
        this.dates = dates;
        this.destination = destination;
    }

    public int getNbPerson() {
       return this.nbPerson;
    }

    public int getDuration() {
        return dates.tripDuration();
    }

    public String getDestination() {
        return this.destination;
    }

    public TripDate getDates() {
        return dates;
    }

}
