package uk.ac.ed.bikerental;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class SystemTests {
    private Location loc1 = new Location("EH8 9RD", "100 Meow St");
    private Customer customer;

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
        DateRange dateRange = new DateRange(LocalDate.of(2019, 1, 7),
                LocalDate.of(2019, 1, 10));
        Collection<BikeType> bikeTypes = new LinkedList<>();
        bikeTypes.add(BikeType.findType("racing"));
        bikeTypes.add(BikeType.findType("mountain"));

        // Send request and retrieve quotes, which should be empty (cannot be fulfilled)
        Collection<Quote> quotes = customer.sendRequest(loc, dateRange, bikeTypes);
        assertEquals(new LinkedList<Quote>(), quotes);
    }

    @Test
    void testGetQuotesSuccessful() {
        // Setup request
        DateRange dateRange = new DateRange(LocalDate.of(2019, 1, 7),
                LocalDate.of(2019, 1, 10));
        Collection<BikeType> bikeTypes = new LinkedList<>();
        bikeTypes.add(BikeType.findType("racing"));
        bikeTypes.add(BikeType.findType("mountain"));

        // Get quote and check that it is correct
        LinkedList<Quote> quotes = customer.sendRequest(loc1, dateRange, bikeTypes);
        assertEquals(1, quotes.size());
        Quote quote = quotes.getFirst();
        assertEquals(new BigDecimal(390).stripTrailingZeros(), quote.getPrice().stripTrailingZeros());
        assertEquals(new BigDecimal(1100).stripTrailingZeros(), quote.getDeposit().stripTrailingZeros());
    }
}
