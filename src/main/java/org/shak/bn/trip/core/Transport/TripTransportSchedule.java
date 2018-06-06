package org.shak.bn.trip.core.Transport;

public class TripTransportSchedule {
    private TripTransportTime go;
    private TripTransportTime back;

    public int transportDuration() {
        return go.getDuration() + back.getDuration();
    }
}
