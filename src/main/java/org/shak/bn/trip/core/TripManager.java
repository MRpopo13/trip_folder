package org.shak.bn.trip.core;

import org.apache.commons.text.StrSubstitutor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TripManager {
    private static final String ROOT_URL = "https://www.booking.com/searchresults.html?ac_suggestion_theme_list_length=0&order=price&";
    private static final String URL_PATTERN = "checkin_month=${m_in}&checkin_monthday=${d_in}&checkin_year=${y_in}&checkout_month=${m_out}&checkout_monthday=${d_out}&checkout_year=${y_out}&class_interval=1&group_adults=${nb_pers}&no_rooms=1&ss=${city}";

    public static void main(String[] args) {
        configProxy();
        List<Trip> trips = Trip.buildTripsFromInfoList();
        long start = System.currentTimeMillis();
        Objects.requireNonNull(trips, "The cities file could not be read, check the file emplacement")
                .parallelStream().forEach(
                TripManager::launchRequestForTrip
        );
        long time = (System.currentTimeMillis() - start) / 1000;
        System.out.println("Execution took " + time + " seconds");
    }

    private static void configProxy() {
        System.setProperty("https.proxyHost", "proxy-fr.trading-lb.gdfsuez.net");
        System.setProperty("https.proxyPort", "8080");
    }


    private static void launchRequestForTrip(Trip trip) {
        String url = buildURLForTrip(trip);
        try {
            Document webPage = Jsoup.connect(url)
                    .timeout(10000)
                    .get();
            String minPrice = getPriceFromHtmlResponse(webPage);
            if (minPrice == null) {
                System.out.println("*********** No price for" + trip.getDestination() + " at date " + trip.getDates() + "***************");
            } else {
                System.out.println("The min cost for " + trip.getDestination() + " at date " + trip.getDates() + " is " + minPrice);
            }
            String htmlString = webPage.html();

            List<String> lines = Collections.singletonList(htmlString);
            Path file = Paths.get(trip.getTripHousingFileName());
            Files.write(file, lines, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String buildURLForTrip(Trip trip) {
        StrSubstitutor substitutor = new StrSubstitutor(trip.getValuesMap());
        return substitutor.replace(ROOT_URL + URL_PATTERN);
    }

    private static String getPriceFromHtmlResponse(Document htmlDoc) {
        Element pageContent = htmlDoc.body();
        Elements prices = pageContent.getElementsByClass("price");
        if (prices == null || prices.first() == null) {
            return null;
        }
        Elements minPrice = prices.first().getElementsByTag("b");
        if (minPrice == null) {
            return null;
        }
        return prices.first().getElementsByTag("b").text().replace("€", "").trim();
    }
}
