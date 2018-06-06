package org.shak.bn.trip.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

public class Trip extends AbstractTrip {
    private static final String HOUSING_FILE_SUFFIX = "_housing.html";
    private static final String FILE_PAGE_PATH = "C:\\Users\\wt5369\\Desktop\\htmlFool\\";
    private int budget;
    private Map<String, String> valuesMap;

    private Trip(TripDate dates, String destination) {
        this(dates, destination, 1);
    }

    private Trip(TripDate dates, String destination, int nbPerson) {
        super(nbPerson, dates, destination);
        valuesMap = new HashMap<>();
        valuesMap.put("d_in", String.valueOf(dates.getCheckInDay()));

        valuesMap.put("m_in", String.valueOf(dates.getCheckInMonth()));
        valuesMap.put("y_in", String.valueOf(dates.getCheckInYear()));
        valuesMap.put("d_out", String.valueOf(dates.getCheckOutDay()));
        valuesMap.put("m_out", String.valueOf(dates.getCheckOutMonth()));
        valuesMap.put("y_out", String.valueOf(dates.getCheckOutYear()));
        valuesMap.put("city", destination);
        valuesMap.put("nb_pers", String.valueOf(nbPerson));
    }

    public static List<Trip> buildTripsFromInfoList(List<TripDate> dates, List<String> cities) {
        List<Trip> trips = new ArrayList<>();
        dates.forEach(
                date -> cities.forEach(
                        city -> trips.add(new Trip(date, city))
                )
        );
        return trips;
    }

    public static List<Trip> buildTripsFromInfoList() {
        List<String> cities = getListCities();
        List<TripDate> dates = TripDate.getTripDatesFromFile("resources/date.txt");
        List<Trip> trips = new ArrayList<>();
        dates.forEach(
                date -> cities.forEach(
                        city -> trips.add(new Trip(date, city))
                )
        );
        return trips;
    }

    public String getTripHousingFileName() {
        return FILE_PAGE_PATH + this.destination + HOUSING_FILE_SUFFIX;
    }

    private static List<String> getListCities() {
        try {
            return Files.readAllLines(new File("resources/cities.txt").toPath(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public Map<String, String> getValuesMap() {
        return valuesMap;
    }

    @Override
    public String toString() {
        return "Trip" +
                "for" + nbPerson +
                "people, dates=" + dates +
                ", to " + destination;
    }
}
