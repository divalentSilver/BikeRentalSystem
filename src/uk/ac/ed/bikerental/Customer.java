package uk.ac.ed.bikerental;

import java.util.Collection;
import java.util.HashMap;

public class Customer {
    private int customerID;
    private HashMap<Integer, Booking> bookings;
    private Collection<Quote> retrievedQuotes;
    private System system;

    public Customer(int ID, System system) {
        customerID = ID;
        this.system = system;
        bookings = new HashMap<>();
    }

    public Collection<Quote> sendRequest(Location location, DateRange dateRange, Collection<BikeType> bikeTypes) {
        return system.getQuotes(location, dateRange, bikeTypes);
    }

    public boolean bookQuote(Quote quote, String paymentInfo, boolean isDelivery) {
        Booking booking = quote.book(paymentInfo, isDelivery);
        if (booking != null) {
            bookings.put(booking.getID(), booking);
            return true;
        }
        return false;
    }
}
