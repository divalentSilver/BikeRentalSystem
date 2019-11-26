package uk.ac.ed.bikerental;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

public class Bike implements Deliverable, Comparable {

    enum BikeStatus {
        atPartner,
        atProvider,
        inRepair,
        inDelivery,
        withCustomer
    }

    private BikeType bikeType;
    private Location location;
    private HashMap<DateRange, BikeStatus> busyDates;

    public Bike(BikeType bikeType, Location location) {
        this.bikeType = bikeType;
        this.location = location;
        this.busyDates = new HashMap<>();
    }

    public BikeType getType() {
        return bikeType;
    }

    public Location getLocation() {
        return location;
    }
    
    public BikeStatus getStatus(DateRange dateRange) {
    	for (DateRange busyRange: busyDates.keySet()) {
            if (busyRange.overlaps(dateRange))
                return busyDates.get(dateRange);
        }
		return null;
    }
    
    public void setStatus(DateRange dateRange, BikeStatus status) {
        busyDates.put(dateRange, status);
    }

    public boolean markFree(DateRange dateRange) {
        busyDates.remove(dateRange);
        return true;
    }

    /**
     * Checks if the bike is busy for the date range that is given
     *
     * @param dateRange to be checked
     * @return true if busy, false if not busy
     */
    public boolean isBusy(DateRange dateRange) {
        for (DateRange busyRange: busyDates.keySet()) {
            if (busyRange.overlaps(dateRange))
                return true;
        }
        return false;
    }

    /**
     * Marks the bike as busy for the specified date range, if the bike is already not busy during the date range
     *
     * @param dateRange that will be marked busy for the bike
     * @return true if successfully marked busy, false if not
     */
    public boolean markBusy(DateRange dateRange, BikeStatus status) {
        if (isBusy(dateRange))
            return false;
        busyDates.put(dateRange, status);
        return true;
    }

    @Override
    public void onPickup() {
    	// find the target date range
    	DateRange pickupDateRange = new DateRange(LocalDate.now(), LocalDate.now());
    	DateRange targetDateRange = null;
    	for (DateRange busyRange: busyDates.keySet()) {
            if (busyRange.overlaps(pickupDateRange)) {
            	targetDateRange = busyRange;
            	break;
            }
        }
    	
    	// set the bike status as 'inDelivery'
        if (targetDateRange != null)
            busyDates.put(targetDateRange, BikeStatus.inDelivery);
    }

    @Override
    public void onDropoff() {
    	// find the target date range
    	DateRange pickupDateRange = new DateRange(LocalDate.now(), LocalDate.now());
    	DateRange targetDateRange = null;
    	Boolean deliveryToPartner = false;
    	for (DateRange busyRange: busyDates.keySet()) {
            if (busyRange.overlaps(pickupDateRange)) {
            	targetDateRange = busyRange;
            	if (targetDateRange.getStart() == busyRange.getStart()) {
            		deliveryToPartner = true;
            	}
            	break;
            }
        }
    	
    	// change the bike status depending on the situation
        if (targetDateRange != null) {
        	if (deliveryToPartner) {
        		// in case of delivering from the original provider to the partner
        		busyDates.put(targetDateRange, BikeStatus.atPartner);
        	} else {
        		// in case of delivering from the partner to the original provider
        		markFree(targetDateRange);
        	}
        }
    }

    @Override
    public int compareTo(Object o) {
        return getType().compareTo(o);
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
        return ((Bike) obj).getType().equals(getType());
    }
}