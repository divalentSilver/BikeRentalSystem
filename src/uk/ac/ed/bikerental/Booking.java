package uk.ac.ed.bikerental;

public class Booking {

    // If this was used in real life, we would want to have a separate module dedicated to handling
    // payment information due to its sensitivity
    private String paymentInfo;
    private boolean completedStatus;

    public Booking(String paymentInfo, boolean isDelivery) {
        this.paymentInfo = paymentInfo;
        completedStatus = false;
    }
}
