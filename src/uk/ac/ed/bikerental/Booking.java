package uk.ac.ed.bikerental;

public class Booking {
    private int ID;
    private Quote quote;
    // If this was used in real life, we would want to have a separate module dedicated to handling
    // payment information due to its sensitivity
    private String paymentInfo;
    private boolean completedStatus;

    public Booking(String paymentInfo, Quote quote, boolean isDelivery) {
        this.paymentInfo = paymentInfo;
        this.quote = quote;
        completedStatus = false;
    }

    public int getID() {
        return ID;
    }
}
