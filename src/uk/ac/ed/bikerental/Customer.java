package uk.ac.ed.bikerental;

import java.util.Collection;

public class Customer {
    private int customerID;
    private Collection<Booking> bookings;
    private Collection<Quote> retrievedQuotes;
    private System system;

    public Customer(int ID, System system) {
        customerID = ID;
        this.system = system;
    }

    public Collection<Quote> sendRequest(Location location, DateRange dateRange, Collection<BikeType> bikeTypes) {
        return system.getQuotes(location, dateRange, bikeTypes);
    }

    public boolean bookQuote(Quote quote, String paymentInfo, boolean isDelivery) {
        Booking booking = quote.book(paymentInfo, isDelivery);
        if (booking != null) {
            bookings.add(booking);
            return true;
        }
        return false;
    }
}
