package uk.ac.ed.bikerental;

import org.junit.jupiter.api.*;

import uk.ac.ed.bikerental.Bike.BikeStatus;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class SystemTests {
    private Location loc1 = new Location("EH8 9RD", "100 Meow St");
    private Location loc2 = new Location("EH8 9RE", "104 Meow St");
    private Customer customer;

    /*
    Compares two lists without order mattering. Objects in the lists must be comparable.
     */
    private boolean equalLists(List a, List b) {
        Collections.sort(a);
        Collections.sort(b);
        return a.equals(b);
    }

    @Test 
    void testEqualLists() {
        LinkedList<Integer> a = new LinkedList<>();
        LinkedList<Integer> b = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            a.add(i);
            b.add(i);
        }
        Collections.shuffle(a);
        Collections.shuffle(b);
        assertTrue(equalLists(a, b));
    }

    @BeforeEach
    void setUp() throws Exception {
        // Clear just in case
        System.getSystem().resetSystem();

        // Setup mock delivery service before each tests
        DeliveryServiceFactory.setupMockDeliveryService();

        // Create a customer
        customer = new Customer();

        // Define the Provider Bill
        PricingPolicy pricingPolicy = new DefaultPricingPolicy();
        ValuationPolicy valuationPolicy = new DefaultValuationPolicy(BigDecimal.TEN);
        Provider bill = new Provider("Bill's", loc1, pricingPolicy, valuationPolicy);

        /// Create the system, add the bikes, and set their prices
        System.getSystem().addProvider(bill);
        bill.addBike(BikeType.setBikeType("racing", new BigDecimal(10000)));
        bill.addBike(BikeType.setBikeType("mountain", new BigDecimal(1000)));
        bill.getPricingPolicy().setDailyRentalPrice(BikeType.findType("racing"), new BigDecimal(100));
        bill.getPricingPolicy().setDailyRentalPrice(BikeType.findType("mountain"), new BigDecimal(30));
        
    }
    
    // TODO: Write system tests covering the three main use cases

    @Test
    void testGetQuotesEmpty() {
        // Setup request
        Location loc = new Location("EZ8 5ZP", "fake");
        DateRange dateRange = new DateRange(LocalDate.of(2019, 1, 7), LocalDate.of(2019, 1, 10));
        List<BikeType> bikeTypes = new LinkedList<>();
        bikeTypes.add(BikeType.findType("racing"));
        bikeTypes.add(BikeType.findType("mountain"));

        // Send request and retrieve quotes, which should be empty (cannot be fulfilled)
        List<Quote> quotes = customer.sendRequest(loc, dateRange, bikeTypes);
        assertEquals(new LinkedList<Quote>(), quotes);
    }

    @Test
    void testGetQuotesSuccessful() {
        // Setup request
        DateRange dateRange = new DateRange(LocalDate.of(2019, 1, 7), LocalDate.of(2019, 1, 10));
        List<BikeType> bikeTypes = new LinkedList<>();
        bikeTypes.add(BikeType.findType("racing"));
        bikeTypes.add(BikeType.findType("mountain"));

        // Get quote and check that it is correct
        LinkedList<Quote> quotes = customer.sendRequest(loc1, dateRange, bikeTypes);
        assertEquals(1, quotes.size());
        Quote quote = quotes.getFirst();
        assertEquals(new BigDecimal(390).stripTrailingZeros(), quote.getPrice().stripTrailingZeros());
        assertEquals(new BigDecimal(1100).stripTrailingZeros(), quote.getDeposit().stripTrailingZeros());
    }
    
    @Test
    void testBookQuoteNoDelivery() {
    	// Setup request
        DateRange dateRange = new DateRange(LocalDate.of(2019, 1, 7), LocalDate.of(2019, 1, 10));
        LinkedList<BikeType> bikeTypes = new LinkedList<>();
        bikeTypes.add(BikeType.findType("racing"));
        bikeTypes.add(BikeType.findType("mountain"));

        // Setup quote
        LinkedList<Quote> quotes = customer.sendRequest(loc1, dateRange, bikeTypes);
        Quote quote = quotes.getFirst();
        String paymentInfo = "This is the payment information.";
        
        // Get booking result and check that it is correct
        Integer bookingID = customer.bookQuote(quote, paymentInfo);
    	Booking booking = customer.getBooking(bookingID);
        assertEquals(dateRange, booking.getDateRange());
    	List<Bike> bookedBikes = booking.getBikes();
    	LinkedList<BikeType> bookedBikeTypes = new LinkedList<>();
    	for(Bike bike: bookedBikes)
    		bookedBikeTypes.add(bike.getType());
    	assertTrue(equalLists(bookedBikeTypes, bikeTypes));
    }
    
    @Test
    void testBookQuoteDelivery() {
    	// Setup request
        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now().plusDays(3));
        LinkedList<BikeType> bikeTypes = new LinkedList<>();
        bikeTypes.add(BikeType.findType("racing"));
        bikeTypes.add(BikeType.findType("mountain"));

        // Setup quote
        LinkedList<Quote> quotes = customer.sendRequest(loc1, dateRange, bikeTypes);
        Quote quote = quotes.getFirst();
        String paymentInfo = "This is the payment information.";
        
        // Get booking result and check that it is correct
        Integer bookingID = customer.bookQuote(quote, paymentInfo, loc1);
    	Booking booking = customer.getBooking(bookingID);
        assertEquals(dateRange, booking.getDateRange());
        List<Bike> bookedBikes = booking.getBikes();
    	LinkedList<BikeType> bookedBikeTypes = new LinkedList<>();
    	for(Bike bike: bookedBikes)
    		bookedBikeTypes.add(bike.getType());
    	assertTrue(equalLists(bookedBikeTypes, bikeTypes));
    	MockDeliveryService deliveryService = (MockDeliveryService)
    			DeliveryServiceFactory.getDeliveryService();
    	assertNotNull(deliveryService.getPickupsOn(dateRange.getStart()));
    }
    
    @Test
    void testReturnToProvider() {
    	// Setup request
        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now().plusDays(3));
        List<BikeType> bikeTypes = new LinkedList<>();
        bikeTypes.add(BikeType.findType("racing"));
        bikeTypes.add(BikeType.findType("mountain"));

        // Setup quote
        LinkedList<Quote> quotes = customer.sendRequest(loc1, dateRange, bikeTypes);
        Quote quote = quotes.getFirst();
        String paymentInfo = "This is the payment information.";
        
        // Setup booking
        Integer bookingID = customer.bookQuote(quote, paymentInfo, loc1);
    	Booking booking = customer.getBooking(bookingID);
        List<Bike> bookedBikes = booking.getBikes();
    	
    	// Check the bike status
    	customer.returnBikes(booking, loc1);
    	for(Bike bike : bookedBikes) {
        	assertFalse(bike.isBusy(dateRange));
    	}

    }
    
    @Test
    void testReturnToPartner() {
    	// Setup request
        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now().plusDays(3));
        List<BikeType> bikeTypes = new LinkedList<>();
        bikeTypes.add(BikeType.findType("racing"));
        bikeTypes.add(BikeType.findType("mountain"));

        // Setup quote
        LinkedList<Quote> quotes = customer.sendRequest(loc1, dateRange, bikeTypes);
        Quote quote = quotes.getFirst();
        String paymentInfo = "This is the payment information.";
        
        // Setup booking
        Integer bookingID = customer.bookQuote(quote, paymentInfo, loc1);
    	Booking booking = customer.getBooking(bookingID);
        List<Bike> bookedBikes = booking.getBikes();

    	// Check the bike status
    	customer.returnBikes(booking, loc2);
    	for(Bike bike: bookedBikes) {
    		assertEquals(bike.getStatus(dateRange), BikeStatus.atPartner);
    	}
    }
}
