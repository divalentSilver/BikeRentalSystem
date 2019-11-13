package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;

public class DefaultPricingPolicy implements PricingPolicy {
    private HashMap<BikeType, BigDecimal> bikePrices = new HashMap<>();

    /**
     * Calculate the price for a single bike for a given duration
     *
     * @param bike to calculate the price for
     * @param duration of the rental
     * @return the price for the customer
     */
    private BigDecimal calculatePrice(Bike bike, DateRange duration) {
        // Retrieve the daily price and multiply by days
        BigDecimal dailyPrice = bikePrices.get(bike.getType());
        assert dailyPrice != null;  // Ensure that the daily price is defined
        BigDecimal price = dailyPrice.multiply(new BigDecimal(duration.toDays()));

        return price;
    }

    /**
     * Set the daily rental price for a type of bike
     *
     * @param bikeType is the type of bike
     * @param dailyPrice is the price to set it at
     */
    @Override
    public void setDailyRentalPrice(BikeType bikeType, BigDecimal dailyPrice) {
        bikePrices.put(bikeType, dailyPrice);
    }

    /**
     * Calculate the total price for several bikes over the given duration
     *
     * @param bikes to be rented
     * @param duration for the bikes to be rented
     * @return the total price
     */
    @Override
    public BigDecimal calculatePrice(Collection<Bike> bikes, DateRange duration) {
        BigDecimal total = new BigDecimal(0);
        for (Bike bike: bikes) {
            total = total.add(calculatePrice(bike, duration));
        }
        return total;
    }
}
