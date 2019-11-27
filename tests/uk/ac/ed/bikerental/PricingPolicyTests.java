package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;


public class PricingPolicyTests {
    MultidayPricingPolicy pricingPolicy;
    BikeType racing, mountain;
    DateRange oneDay, twoDays, threeDays;

    @BeforeEach
    void setUp() throws Exception {
        oneDay = new DateRange(LocalDate.ofYearDay(2000, 1),
                LocalDate.ofYearDay(2000, 1));
        twoDays = new DateRange(LocalDate.ofYearDay(2000, 1),
                LocalDate.ofYearDay(2000, 2));
        threeDays = new DateRange(LocalDate.ofYearDay(2000, 1),
                LocalDate.ofYearDay(2000, 3));

        pricingPolicy = new MultidayPricingPolicy();
        racing = BikeType.setBikeType("racing", new BigDecimal(10000));
        mountain = BikeType.setBikeType("mountain", new BigDecimal(1000));

        pricingPolicy.setDailyRentalPrice(racing, new BigDecimal(100));
        pricingPolicy.setDailyRentalPrice(mountain, new BigDecimal(10));
    }
    
    @Test
    void testSingleDiscount() {
        Collection<Bike> bikes = new LinkedList<>();
        bikes.add(new Bike(racing, new Location("EH8 9RD", "beep boop")));
        bikes.add(new Bike(racing, new Location("EH8 9RD", "boop bop")));
        bikes.add(new Bike(racing, new Location("EH8 9RD", "100k")));

        pricingPolicy.setDiscount(2l, new BigDecimal(10));

        assertEquals(new BigDecimal(300).stripTrailingZeros(),
                pricingPolicy.calculatePrice(bikes, oneDay).stripTrailingZeros());
        assertEquals(new BigDecimal(540).stripTrailingZeros(),
                pricingPolicy.calculatePrice(bikes, twoDays).stripTrailingZeros());
        assertEquals(new BigDecimal(810).stripTrailingZeros(),
                pricingPolicy.calculatePrice(bikes, threeDays).stripTrailingZeros());

        bikes.add(new Bike(mountain, new Location("EH9 7UH", "")));
        assertEquals(new BigDecimal(310).stripTrailingZeros(),
                pricingPolicy.calculatePrice(bikes, oneDay).stripTrailingZeros());
        assertEquals(new BigDecimal(558).stripTrailingZeros(),
                pricingPolicy.calculatePrice(bikes, twoDays).stripTrailingZeros());
    }

    @Test
    void testMultiDiscount() {
        Collection<Bike> bikes = new LinkedList<>();
        bikes.add(new Bike(racing, new Location("EH8 9RD", "beep boop")));
        bikes.add(new Bike(racing, new Location("EH8 9RD", "boop bop")));
        bikes.add(new Bike(racing, new Location("EH8 9RD", "100k")));

        pricingPolicy.setDiscount(1l, new BigDecimal(10));  // Normal discount from the first day
        pricingPolicy.setDiscount(2l, new BigDecimal(0));   // No discount from the second day
        pricingPolicy.setDiscount(3l, new BigDecimal(100)); // Free from the third day

        assertEquals(new BigDecimal(270).stripTrailingZeros(),
                pricingPolicy.calculatePrice(bikes, oneDay).stripTrailingZeros());
        assertEquals(new BigDecimal(600).stripTrailingZeros(),
                pricingPolicy.calculatePrice(bikes, twoDays).stripTrailingZeros());
        assertEquals(BigDecimal.ZERO.stripTrailingZeros(),
                pricingPolicy.calculatePrice(bikes, threeDays).stripTrailingZeros());
        
        bikes.add(new Bike(mountain, new Location("EH9 7UH", "")));
        assertEquals(new BigDecimal(279).stripTrailingZeros(),
                pricingPolicy.calculatePrice(bikes, oneDay).stripTrailingZeros());
        assertEquals(new BigDecimal(620).stripTrailingZeros(),
                pricingPolicy.calculatePrice(bikes, twoDays).stripTrailingZeros());
        assertEquals(BigDecimal.ZERO.stripTrailingZeros(),
                pricingPolicy.calculatePrice(bikes, threeDays).stripTrailingZeros());
    }
}