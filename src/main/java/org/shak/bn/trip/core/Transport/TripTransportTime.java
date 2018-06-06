package org.shak.bn.trip.core.Transport;

import java.time.LocalDateTime;

public class TripTransportTime {
    private LocalDateTime arrival;
    private LocalDateTime departure;

    public int getDuration() {
        return (arrival.getHour() - departure.getHour())%24 + (arrival.getMinute() - departure.getMinute())%60;
    }
}
