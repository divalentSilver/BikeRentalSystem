package uk.ac.ed.bikerental;

public class Booking {
    private static int IDCounter = 0;

    private int ID;
    private Quote quote;
    // If this was used in real life, we would want to have a separate module dedicated to handling
    // payment information due to its sensitivity
    private String paymentInfo;
    private boolean completedStatus;

    public Booking(Quote quote, String paymentInfo) {
        this.ID = IDCounter++;
        this.quote = quote;
        this.paymentInfo = paymentInfo;
        this.completedStatus = false;
    }

    public int getID() {
        return ID;
    }
}
