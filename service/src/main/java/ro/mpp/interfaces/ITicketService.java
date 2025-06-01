package ro.mpp.interfaces;

import ro.mpp.Flight;
import ro.mpp.IService;
import ro.mpp.Ticket;
import ro.mpp.ValidationException;

public interface ITicketService extends IService<Integer, Ticket> {
    Iterable<Ticket> findByFlight(Flight flight) throws ValidationException;
}
