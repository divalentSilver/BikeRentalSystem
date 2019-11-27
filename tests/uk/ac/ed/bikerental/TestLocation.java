package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class TestLocation {
	private Location loc1, loc2, loc3;
	
    @BeforeEach
    void setUp() throws Exception {
        this.loc1 = new Location("EH8 9RD", "100 Meow St");
        this.loc2 = new Location("EH8 9RE", "104 Meow St");
        this.loc3 = new Location("ED8 4GE", "104 Meow St");
    }
    
    // Sample JUnit tests checking toYears works
    @Test
    void testIsNearToTrue() {
    	assertTrue(loc1.isNearTo(loc2));
    }
    
    @Test
    void testIsNearToFalse() {
    	assertFalse(loc1.isNearTo(loc3));
    }
}
