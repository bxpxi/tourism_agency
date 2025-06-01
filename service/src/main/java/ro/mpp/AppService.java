package ro.mpp;

import ro.mpp.interfaces.IEmployeeService;
import ro.mpp.interfaces.IFlightService;
import ro.mpp.interfaces.ITicketService;
import ro.mpp.observer.Observer;

public class AppService implements IAppService {
    private final IEmployeeService employeeService;
    private final IFlightService flightService;
    private final ITicketService ticketService;

    public AppService(IEmployeeService employeeService, IFlightService flightService, ITicketService ticketService) {
        this.employeeService = employeeService;
        this.flightService = flightService;
        this.ticketService = ticketService;
    }

    public IEmployeeService getEmployeeService() {
        return employeeService;
    }

    public IFlightService getFlightService() {
        return flightService;
    }

    public ITicketService getTicketService() {
        return ticketService;
    }

    @Override
    public Employee login(String agencyName, String password, Observer client) throws ValidationException {
        try {
            return employeeService.findByAgencyNameAndPassword(agencyName, password);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public void signin(String agencyName, String password) throws ValidationException {
        try {
            employeeService.save(new Employee(agencyName, password));
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public Iterable<Flight> getAllFlights() throws ValidationException {
        try {
            return flightService.findAll();
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public Iterable<Flight> findByDestinationAndDepartureDate(String destination, String departureDate) throws ValidationException {
        try {
            return flightService.findByDestinationAndDepartureDate(destination, departureDate);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public void buyTicket(String client, Flight flight, int noOfSeats) throws ValidationException {
        if(noOfSeats > flight.getAvailableSeats()) {
            throw new ValidationException("Number of seats exceeded");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - noOfSeats);
        try {
            flightService.update(flight);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }

        Ticket ticket = new Ticket(client, flight, noOfSeats);
        try {
            ticketService.save(ticket);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public void logout(Employee employee) {

    }
}
