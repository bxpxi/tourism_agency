package ro.mpp.interfaces;

import ro.mpp.Flight;
import ro.mpp.IRepository;

public interface IFlightRepository extends IRepository<Integer, Flight> {
    Iterable<Flight> findByDestinationAndDepartureDate(String destination, String departureDate);
}
