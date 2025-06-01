package ro.mpp.interfaces;

import ro.mpp.Flight;
import ro.mpp.IRepository;
import ro.mpp.Ticket;

public interface ITicketRepository extends IRepository<Integer, Ticket> {
    Iterable<Ticket> findByFlight(Flight flight);
}