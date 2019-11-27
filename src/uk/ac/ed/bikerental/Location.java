package uk.ac.ed.bikerental;

public class Location {
	/**
	 * The string which post code of the location is stored.
	 */
    private String postcode;
    /**
     * The string which address of the location is stored.
     */
    private String address;

    public Location(String postcode, String address) {
        assert postcode.length() >= 6;
        this.postcode = postcode;
        this.address = address;
    }

    /**
     * This method compares two locations and determines if they are near enough for collection/delivery of bikes.
     * We do this by simply comparing the first two characters in their postal codes.
     *
     * @param other location to be compared against
     * @return true if near, otherwise false
     */
    public boolean isNearTo(Location other) {
        return this.getPostcode().substring(0, 2).equalsIgnoreCase(
              other.getPostcode().substring(0, 2));
    }

    /**
     * Get the postal code of the location in a String format
     * @return postal code
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * Get the address of the location in a String format
     * @return address
     */
    public String getAddress() {
        return address;
    }

}
