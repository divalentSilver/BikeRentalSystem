package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.HashMap;

public class BikeType implements Comparable {
    // To avoid duplicate BikeTypes, keep a static map of them
    private static HashMap<String, BikeType> bikeTypes = new HashMap<>();
    private BigDecimal replacementValue;
    private String name;

    // This constructor is private because external classes should never directly initialize it
    private BikeType(BigDecimal replacementValue, String name) {
        this.replacementValue = replacementValue;
        this.name = name;
    }

    // Look up an existing BikeType
    public static BikeType findType(String name) {
        return bikeTypes.get(name);
    }

    // Create a new BikeType or set the replacement value of an existing one
    public static BikeType setBikeType(String name, BigDecimal replacementValue) {
        // Ensure that the replacement value is greater than 0
        assert replacementValue.compareTo(BigDecimal.ZERO) > 0;

        // If a BikeType already exists, update the entry with the new replacement value.
        // Otherwise, add a new one
        if (bikeTypes.containsKey(name))
            bikeTypes.get(name).replacementValue = replacementValue;
        else
            bikeTypes.put(name, new BikeType(replacementValue, name));
        return findType(name);
    }

    public BigDecimal getReplacementValue() {
        return replacementValue;
    }

    @Override
    public int compareTo(Object o) {
        assert getClass() == o.getClass();
        return name.compareTo(((BikeType) o).name);
    }

    @Override
    public boolean equals(Object obj) {
        // equals method for testing equality in tests
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return name.equals(((BikeType) obj).name);
    }
}