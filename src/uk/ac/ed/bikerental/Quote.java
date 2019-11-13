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
        price = provider.getPricingPolicy().calculatePrice(bikes, dateRange);
        deposit = BigDecimal.ZERO;
        for (Bike bike: bikes)
            // Calculate the deposit of each bike and sum them up
            // TODO: Maybe move into valuation policy class
            deposit = deposit.add(provider.getValuationPolicy().calculateValue(bike, dateRange.getStart()));
    }

    // TODO: Implement book
}
