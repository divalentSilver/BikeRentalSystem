package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class MultidayPricingPolicy extends DefaultPricingPolicy {
    // We use a TreeMap for the ability to retrieve the entry below a key if it does not exist
    private TreeMap<Long, BigDecimal> discounts = new TreeMap<>();


    /**
     * Calculates the final price that a customer pays
     *
     * @param price set by provider
     * @param discount that needs to be applied to the price
     * @return the final price that will be charged to the customer
     */
    private static BigDecimal applyDiscount(BigDecimal price, BigDecimal discount) {
        return price.multiply(discount).movePointLeft(2);
    }

    /**
     * Calculate the total price for several bikes over the given duration. Discount is applied
     *
     * @param bikes to be rented
     * @param duration for the bikes to be rented
     * @return the total price
     */
    @Override
    public BigDecimal calculatePrice(Collection<Bike> bikes, DateRange duration) {
        BigDecimal total = super.calculatePrice(bikes, duration);

        // Apply a discount if it exists
        // We use the floorEntry to find the discount that is closest to the date range
        Map.Entry<Long, BigDecimal> discount = discounts.floorEntry(duration.toDays());
        if (discount != null)
            total = applyDiscount(total, discount.getValue());

        return total;
    }

    /**
     * Set the day at which a specified discount begins
     *
     * @param startDay of the discount
     * @param percentage of the discount
     */
    public void setDiscount(Long startDay, BigDecimal percentage) {
        // The Provider should not be able to set a negative discount
        assert percentage.abs().equals(percentage);
        discounts.put(startDay, percentage);
    }

    /**
     * Reset the discounts
     */
    public void clearDiscounts() {
        discounts.clear();
    }

}
