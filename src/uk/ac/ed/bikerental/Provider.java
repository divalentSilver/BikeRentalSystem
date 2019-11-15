package uk.ac.ed.bikerental;

import java.util.Collection;
import java.util.LinkedList;

public class Provider {
    private String name;
    private Location location;
    private Collection<Bike> bikes;
    private PricingPolicy pricingPolicy;
    private ValuationPolicy valuationPolicy;

    public Provider(String name, Location location, PricingPolicy pricingPolicy, ValuationPolicy valuationPolicy) {
        this.name = name;
        this.location = location;
        bikes = new LinkedList<>();

        // These policies may differ from Provider to Provider, so take in as argument
        this.pricingPolicy = pricingPolicy;
        this.valuationPolicy = valuationPolicy;
    }

    public Location getLocation() {
        return location;
    }

    public PricingPolicy getPricingPolicy() {
        return pricingPolicy;
    }

    public ValuationPolicy getValuationPolicy() {
        return valuationPolicy;
    }

    public void addBike(BikeType bikeType) {
        bikes.add(new Bike(bikeType, location));
    }

    /**
     * Search for bikes to fulfill the request that is given to the Provider. If the Provider is able to find bikes
     * that completely fulfill the request, and the request itself has at least one bike, then it will return a Quote.
     * Otherwise, null will be returned
     *
     * @param dateRange to look for the bikes
     * @param bikeRequests is a collection of BikeTypes to be fulfillled
     * @return a Quote if the request can be fulfilled, otherwise null
     */
    public Quote generateQuote(DateRange dateRange, Collection<BikeType> bikeRequests) {
        Collection<Bike> validBikes = new LinkedList<>();
        for (Bike bike: bikes)
            // If bike is right type and not busy, save and remove from the list of bike requests
            if (!bike.isBusy(dateRange) && bikeRequests.contains(bike.getType())) {
                validBikes.add(bike);
                bikeRequests.remove(bike.getType());
            }
        // If bike requests have all been fulfilled, return Quote, otherwise null
        if (bikeRequests.isEmpty() && !validBikes.isEmpty())
            return new Quote(this, dateRange, validBikes);
        return null;
    }

    // TODO: Implement addBooking

}
