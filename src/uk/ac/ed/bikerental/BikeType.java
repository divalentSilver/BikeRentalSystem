package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BikeType {
    // To avoid duplicate BikeTypes, keep a static map of them
    private static HashMap<String, BikeType> bikeTypes;

    private BigDecimal replacementValue;

    public BikeType(String name, BigDecimal replacementValue) {
        // Ensure that the replacement value is greater than 0
        assert replacementValue.compareTo(BigDecimal.ZERO) > 0;
        this.replacementValue = replacementValue;
        bikeTypes.put(name, this);
    }

    public BigDecimal getReplacementValue() {
        return replacementValue;
    }

    public BikeType getTypeFromString(String name) {
        return bikeTypes.get(name);
    }
}