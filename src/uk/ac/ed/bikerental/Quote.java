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
            deposit = deposit.add(provider.getValuationPolicy().calculateValue(bike, dateRange.getStart()));
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

    // TODO: Implement book
}
