package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestDateRange {
    private DateRange dateRange1, dateRange2, dateRange3, dateRange4, dateRange5;

    @BeforeEach
    void setUp() throws Exception {
        // Setup resources before each test
        this.dateRange1 = new DateRange(LocalDate.of(2019, 1, 7),
                LocalDate.of(2019, 1, 10));
        this.dateRange2 = new DateRange(LocalDate.of(2019, 1, 5),
                LocalDate.of(2019, 1, 23));
        this.dateRange3 = new DateRange(LocalDate.of(2015, 1, 7),
                LocalDate.of(2018, 1, 10));
        this.dateRange4 = new DateRange(LocalDate.of(2015, 1, 1),
                LocalDate.of(2015, 1, 7));
        this.dateRange5 = new DateRange(LocalDate.of(2015, 1, 2),
                LocalDate.of(2015, 1, 7));
    }

    // Sample JUnit tests checking toYears works
    @Test
    void testToYears1() {
        assertEquals(0, this.dateRange1.toYears());
    }

    @Test
    void testToYears3() {
        assertEquals(3, this.dateRange3.toYears());
    }

    @Test
    void testToDays() {
        assertEquals(1100, this.dateRange3.toDays());
    }

    @Test
    void testOverlapsTrue() {
        assertTrue(dateRange1.overlaps(dateRange2));
        assertTrue(dateRange3.overlaps(dateRange4));    // Overlap of a single day
        assertTrue(dateRange1.overlaps(dateRange1));    // Overlap with itself
        assertTrue(dateRange4.overlaps(dateRange5));    // Almost the same, except one day longer for date range 5
    }

    @Test
    void testOverlapsFalse() {
        assertFalse(dateRange1.overlaps(dateRange3));
        assertFalse(dateRange2.overlaps(dateRange3));
    }

}
