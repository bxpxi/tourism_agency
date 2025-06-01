package ro.mpp.observer;

import ro.mpp.Flight;
import ro.mpp.Ticket;

public interface Observer {
    void updatedFlight(Flight flight);
}
