package uk.ac.ed.bikerental;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class SystemTests {
    private System system;
    private Location loc1 = new Location("EH8 9RD", "100 Meow St");

    @BeforeEach
    void setUp() throws Exception {
        // Setup mock delivery service before each tests
        DeliveryServiceFactory.setupMockDeliveryService();

        // Define the Provider Bill
        PricingPolicy pricingPolicy = new DefaultPricingPolicy();
        ValuationPolicy valuationPolicy = new DefaultValuationPolicy(BigDecimal.TEN);
        Provider bill = new Provider("Bill's", loc1, pricingPolicy, valuationPolicy);

        // Define the bikes
        Bike racing = new Bike(BikeType.setBikeType("racing", new BigDecimal(10000)));
        Bike mountain = new Bike(BikeType.setBikeType("mountain", new BigDecimal(1000)));

        /// Create the system, add the bikes, and set their prices
        system = new System();
        system.addProvider(bill);
        bill.addBike(racing);
        bill.addBike(mountain);
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
        Collection<Quote> quotes = system.getQuotes(loc, dateRange, bikeTypes);
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
        LinkedList<Quote> quotes = system.getQuotes(loc1, dateRange, bikeTypes);
        assertEquals(1, quotes.size());
        Quote quote = quotes.getFirst();
        assertEquals(new BigDecimal(390).stripTrailingZeros(), quote.getPrice().stripTrailingZeros());
        assertEquals(new BigDecimal(1100).stripTrailingZeros(), quote.getDeposit().stripTrailingZeros());
    }
}
