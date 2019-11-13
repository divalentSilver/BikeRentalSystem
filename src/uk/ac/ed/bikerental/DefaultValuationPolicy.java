package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DefaultValuationPolicy implements ValuationPolicy {
    private static final BigDecimal HUNDRED = new BigDecimal(100);
    private BigDecimal depositRate;

    // Can be specified as either a decimal or a percentage. If the value given is greater than or equal to 1,
    // it is treated a percentage.
    public DefaultValuationPolicy(BigDecimal depositRate) {
        if (depositRate.compareTo(BigDecimal.ONE) >= 0)
            depositRate = depositRate.divide(HUNDRED);
        this.depositRate = depositRate;
    }

    @Override
    public BigDecimal calculateValue(Bike bike, LocalDate date) {
        return bike.getType().getReplacementValue().multiply(depositRate);
    }
}
