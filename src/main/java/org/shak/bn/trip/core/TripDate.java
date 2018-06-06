package org.shak.bn.trip.core;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TripDate {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MM yyyy");
    private LocalDate arrival;
    private LocalDate departure;

    public TripDate(LocalDate arrival, LocalDate departure) {
        this.arrival = arrival;
        this.departure = departure;
    }

    public static List<TripDate> getTripDatesFromFile(String filePath) {
        List<String[]> entries = new ArrayList<>();
        List<TripDate> tripDates = new ArrayList<>();

        try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            entries = reader.readAll();
            entries.remove(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        entries.parallelStream().forEach(
                entry -> {
                    tripDates.add(getTripDateFromDatesStrings(entry));

                }
        );
        return tripDates;
    }

    private static TripDate getTripDateFromDatesStrings(String[] entry) {
        LocalDate arrival = LocalDate.parse(entry[0], DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate departure = LocalDate.parse(entry[1], DateTimeFormatter.BASIC_ISO_DATE);
        return new TripDate(arrival, departure);
    }

    public int tripDuration() {
        return Period.between(arrival, departure).getDays();
    }

    public int getCheckInDay() {
        return arrival.getDayOfMonth();
    }

    public int getCheckInMonth() {
        return arrival.getMonthValue();
    }

    public int getCheckInYear() {
        return arrival.getYear();
    }

    public int getCheckOutDay() {
        return departure.getDayOfMonth();
    }

    public int getCheckOutMonth() {
        return departure.getMonthValue();
    }

    public int getCheckOutYear() {
        return departure.getYear();
    }

    @Override
    public String toString() {
        return "From " + arrival.format(DATE_FORMAT) + " To " + departure.format(DATE_FORMAT);
    }
}
