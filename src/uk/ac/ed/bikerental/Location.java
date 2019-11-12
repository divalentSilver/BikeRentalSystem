package uk.ac.ed.bikerental;

public class Location {
    private String postcode;
    private String address;

    public Location(String postcode, String address) {
        assert postcode.length() >= 6;
        this.postcode = postcode;
        this.address = address;
    }

    /**
     * This method compares two locations and determines if they are near enough for collection/delivery of bikes.
     * We do this by simply comparing the first two characters in their postal codes.
     * @param other location to be compared against
     * @return true if near, otherwise false
     */
    public boolean isNearTo(Location other) {
        return this.getPostcode().substring(0, 2).equalsIgnoreCase(
              other.getPostcode().substring(0, 2));
    }

    public String getPostcode() {
        return postcode;
    }

    public String getAddress() {
        return address;
    }

    // You can add your own methods here
}
