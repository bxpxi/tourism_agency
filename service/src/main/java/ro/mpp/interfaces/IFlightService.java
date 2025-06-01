package ro.mpp.interfaces;

import ro.mpp.Flight;
import ro.mpp.ValidationException;
import ro.mpp.IService;

public interface IFlightService extends IService<Integer, Flight> {
    Iterable<Flight> findByDestinationAndDepartureDate(String destination, String departureDate) throws ValidationException;
}