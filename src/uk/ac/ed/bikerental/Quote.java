package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.Collection;

public class Quote {
    private Provider provider;
    private BigDecimal price;
    private BigDecimal deposit;
    private DateRange dateRange;
    private Collection<Bike> bikes;

    public Quote(Provider provider, DateRange dateRange, Collection<Bike> bikes) {
        this.provider = provider;
        this.bikes = bikes;
        this.dateRange = dateRange;

        // Calculate price and deposit
        price = getProvider().getPricingPolicy().calculatePrice(bikes, dateRange);
        deposit = BigDecimal.ZERO;
        for (Bike bike: getBikes())
            // Calculate the deposit of each bike and sum them up
            deposit = deposit.add(provider.getValuationPolicy().calculateValue(bike, getDateRange().getStart()));
    }

    public Provider getProvider() {
        return provider;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public Collection<Bike> getBikes() {
        return bikes;
    }

    /**
     * This method books the quote and returns a booking, if successful. If a delivery location is specified,
     * a delivery will also be scheduled for the beginning of the rental range.
     *
     * @param paymentInfo to pay for the booking
     * @param deliveryLocation, null if no delivery is desired, otherwise the location of where the bikes should be
     *                          dropped off
     * @return booking if quote is successfully booked, else null
     */
    public Booking book(String paymentInfo, Location deliveryLocation) {
        // Check if bikes are still free
        for (Bike bike: getBikes())
            if (bike.isBusy(getDateRange()))
                return null;
        // Now mark the bikes are busy
        for (Bike bike: getBikes())
            bike.markBusy(getDateRange(), Bike.BikeStatus.withCustomer);
        // Schedule delivery, if desired
        if (deliveryLocation != null) {
            for (Bike bike: getBikes())
                DeliveryServiceFactory.getDeliveryService().scheduleDelivery(
                        bike,
                        bike.getLocation(),
                        deliveryLocation,
                        getDateRange().getStart()
                );
        }
        return new Booking(this, paymentInfo);
    }
}
