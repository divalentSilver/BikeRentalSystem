package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MultidayPricingPolicy implements PricingPolicy {
    private HashMap<BikeType, BigDecimal> bikePrices = new HashMap<>();
    private TreeMap<Long, BigDecimal> discounts = new TreeMap<>();


    // Private Methods

    /**
     * Calculates the final price that a customer pays
     * @param price set by provider
     * @param discount that needs to be applied to the price
     * @return the final price that will be charged to the customer
     */
    private static BigDecimal applyDiscount(BigDecimal price, BigDecimal discount) {
        return price.multiply(discount).movePointLeft(2);
    }

    /**
     * Calculate the price for a single bike for a given duration
     * @param bike to calculate the price for
     * @param duration of the rental
     * @return the price for the customer
     */
    private BigDecimal calculatePrice(Bike bike, DateRange duration) {
        // Retrieve the price and discount from the maps
        BigDecimal price = bikePrices.get(bike.getType());
        Map.Entry<Long, BigDecimal> discount = discounts.floorEntry(duration.toDays());
        assert discount != null;
        assert price != null;

        return applyDiscount(price, discount.getValue());
    }


    // Public Methods

    /**
     * Set the daily rental price for a type of bike
     * @param bikeType is the type of bike
     * @param dailyPrice is the price to set it at
     */
    @Override
    public void setDailyRentalPrice(BikeType bikeType, BigDecimal dailyPrice) {
        bikePrices.put(bikeType, dailyPrice);
    }

    /**
     * Calculate the total price for several bikes over the given duration
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

    /**
     * Set the day at which a specified discount begins
     * @param startDay of the discount
     * @param percentage of the discount
     */
    public void setDiscount(Long startDay, BigDecimal percentage) {
        discounts.put(startDay, percentage);
    }

    /**
     * Reset the discounts
     */
    public void clearDiscounts() {
        discounts.clear();
    }

}
