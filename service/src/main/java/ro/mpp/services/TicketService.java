package ro.mpp.services;

import ro.mpp.AbstractService;
import ro.mpp.Flight;
import ro.mpp.Ticket;
import ro.mpp.ValidationException;
import ro.mpp.interfaces.ITicketRepository;
import ro.mpp.interfaces.ITicketService;

public class TicketService extends AbstractService<Integer, Ticket, ITicketRepository> implements ITicketService {
    public TicketService(ITicketRepository repository) {
        super(repository);
    }

    @Override
    public Iterable<Ticket> findByFlight(Flight flight) throws ValidationException {
        return getRepository().findByFlight(flight);
    }
}
