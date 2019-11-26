package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.Collection;

import uk.ac.ed.bikerental.Bike.BikeStatus;

public class Booking {
    private static int IDCounter = 0;

    private int ID;
    private Quote quote;

    // If this was used in real life, we would want to have a separate module dedicated to handling
    // payment information due to its sensitivity
    private String paymentInfo;

    public Booking(Quote quote, String paymentInfo) {
        this.ID = IDCounter++;
        this.quote = quote;
        this.paymentInfo = paymentInfo;

        // Store itself in the provider's bookings
        getProvider().addBooking(this);
    }

    public int getID() {
        return ID;
    }

    public Provider getProvider() {
        return quote.getProvider();
    }

    public BigDecimal getPrice() {
        return quote.getPrice();
    }

    public BigDecimal getDeposit() {
        return quote.getDeposit();
    }

    public DateRange getDateRange() {
        return quote.getDateRange();
    }

    public Collection<Bike> getBikes() {
        return quote.getBikes();
    }
    
    public void markReturned(Location returnLocation) {
    	Collection<Bike> bikes = getBikes();
    	for (Bike bike: bikes) {
    		if (returnLocation == bike.getLocation()) {
    			// when the customer returns the bikes to the original provider
    			bike.markFree(getDateRange());
    		}
    		else {
    			// when the customer returns the bikes to the partner
    			bike.setStatus(getDateRange(), BikeStatus.atPartner);
    		}
    	}
    	this.returnDeposit(paymentInfo);
    }
    
    private void returnDeposit(String paymentInfo) {
    	
    }
    
}
