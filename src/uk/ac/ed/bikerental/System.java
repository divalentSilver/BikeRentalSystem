package uk.ac.ed.bikerental;

import java.util.Collection;
import java.util.LinkedList;

public class System {
    private static System system = new System();
    private Collection<Provider> providers;

    private System(){
        providers = new LinkedList<>();
    }

    public static System getSystem() {
        return system;
    }

    public LinkedList<Quote> getQuotes(Location location, DateRange dateRange, Collection<BikeType> bikes) {
        // Assertions to ensure the user includes valid information
        assert location != null;
        assert dateRange != null;
        assert bikes != null && !bikes.isEmpty();

        LinkedList<Quote> ret = new LinkedList<>();
        for (Provider provider: providers)
            // Only add quotes from providers that are close enough
            if (provider.getLocation().isNearTo(location)) {
                // If a quote is generated, add to the list of quotes
                Quote quote= provider.generateQuote(dateRange, bikes);
                if (quote != null)
                    ret.add(quote);
            }
        return ret;
    }

    public void addProvider(Provider provider) {
        providers.add(provider);
    }

    public void resetSystem() {
        system = new System();
    }
}
