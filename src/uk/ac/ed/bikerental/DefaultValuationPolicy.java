package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DefaultValuationPolicy implements ValuationPolicy {
    private BigDecimal depositRate;

    // Can

    /**
     * Create a new DefaultValuationPolicy. The deposit rate can be specified as either a decimal or a percentage.
     * If the value given is greater than or equal to 1, it is treated a percentage.
     *
     * @param depositRate, either a percentage or decimal
     */
    public DefaultValuationPolicy(BigDecimal depositRate) {
        if (depositRate.compareTo(BigDecimal.ONE) >= 0)
            // Convert percentages to decimals for simplicity
            depositRate = depositRate.movePointLeft(2);
        this.depositRate = depositRate;
    }

    @Override
    public BigDecimal calculateValue(Bike bike, LocalDate date) {
        return bike.getType().getReplacementValue().multiply(depositRate);
    }
}
