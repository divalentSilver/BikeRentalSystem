package uk.ac.ed.bikerental;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class Customer {
    private static int IDCounter = 0;

    private int customerID;
    private HashMap<Integer, Booking> bookings;

    public Customer() {
        customerID = IDCounter++;
        bookings = new HashMap<>();
    }

    public int getCustomerID() {
        return customerID;
    }

    public Booking getBooking(Integer ID) {
        return bookings.get(ID);
    }

    /**
     * This retrieves quotes that fit the request's specifications, if they exist
     *
     * @param location where the customer wants the bikes
     * @param dateRange of the rental
     * @param bikeTypes desired
     * @return the quotes returned by the providers
     */
    public LinkedList<Quote> sendRequest(Location location, DateRange dateRange, Collection<BikeType> bikeTypes) {
        return System.getSystem().getQuotes(location, dateRange, bikeTypes);
    }

    /**
     * This method books the specified quote without delivery. Equivalent to calling bookQuote with delivery,
     * but with the location specified as null.
     *
     * @param quote to be booked
     * @param paymentInfo to pay for the booking
     * @return true if successful, false if not
     */
    public boolean bookQuote(Quote quote, String paymentInfo) {
        return bookQuote(quote, paymentInfo, null);
    }

    /**
     * This method books the specified quote with delivery
     *
     * @param quote to be booked
     * @param paymentInfo to pay for the booking
     * @param deliveryLocation for the delivery to drop off the bikes
     * @return true if successful, false if not
     */
    public boolean bookQuote(Quote quote, String paymentInfo, Location deliveryLocation) {
        Booking booking = quote.book(paymentInfo, deliveryLocation);
        if (booking != null) {
            bookings.put(booking.getID(), booking);
            return true;
        }
        return false;
    }
}
