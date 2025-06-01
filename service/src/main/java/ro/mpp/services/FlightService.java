package ro.mpp.services;

import ro.mpp.AbstractService;
import ro.mpp.Flight;
import ro.mpp.ValidationException;
import ro.mpp.interfaces.IFlightRepository;
import ro.mpp.interfaces.IFlightService;

public class FlightService extends AbstractService<Integer, Flight, IFlightRepository> implements IFlightService {
    public FlightService(IFlightRepository repository) {
        super(repository);
    }

    @Override
    public Iterable<Flight> findByDestinationAndDepartureDate(String destination, String departureDate) throws ValidationException {
        return getRepository().findByDestinationAndDepartureDate(destination, departureDate);
    }
}