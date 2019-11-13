package uk.ac.ed.bikerental;

import java.util.Collection;
import java.util.LinkedList;

public class Bike {
    private BikeType bikeType;
    private Collection<DateRange> busyDates;

    public Bike(BikeType bikeType) {
        this.bikeType = bikeType;
        this.busyDates = new LinkedList<>();
    }

    public BikeType getType() {
        return bikeType;
    }

    /**
     * Checks if the bike is busy for the date range that is given
     * @param dateRange to be checked
     * @return true if busy, false if not busy
     */
    public boolean isBusy(DateRange dateRange) {
        for (DateRange busyRange: busyDates) {
            if (busyRange.overlaps(dateRange))
                return true;
        }
        return false;
    }

    /**
     * Marks the bike as busy for the specified date range, if the bike is already not busy during the date range
     * @param dateRange that will be marked busy for the bike
     * @return true if successfully marked busy, false if not
     */
    public boolean markBusy(DateRange dateRange) {
        if (isBusy(dateRange))
            return false;
        return busyDates.add(dateRange);
    }
}