package ro.mpp;

import ro.mpp.observer.Observer;

public interface IAppService {
    //Employee login(String agencyName, String password) throws ValidationException;
    void signin(String agencyName, String password) throws ValidationException;
    Iterable<Flight> getAllFlights() throws ValidationException;
    Iterable<Flight> findByDestinationAndDepartureDate(String destination, String departureDate) throws ValidationException;
    void buyTicket(String client, Flight flight, int noOfSeats) throws ValidationException;
    void logout(Employee employee);
    Employee login(String agencyName, String password, Observer client) throws ValidationException;
}

