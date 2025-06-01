package ro.mpp.server;

import ro.mpp.Employee;
import ro.mpp.Flight;
import ro.mpp.IAppService;
import ro.mpp.ValidationException;
import ro.mpp.observer.Observer;

import java.util.HashMap;

public class ServiceImplementation implements IAppService {
    private final IAppService innerService;
    HashMap<String, Observer> employeesObs = new HashMap<>();

    public ServiceImplementation(IAppService innerService) {
        this.innerService = innerService;
    }

    @Override
    public Employee login(String agencyName, String password, Observer client) throws ValidationException {
        var employee = innerService.login(agencyName, password, client);
        System.out.println("Login employee");
        if(employee == null) {
            System.out.println("Employee is null");
        }
        if(client == null) {
            System.out.println("Client is null");
        }
        if(employee != null && client != null) {
            if(employeesObs.containsKey(agencyName)) {
                throw new ValidationException("Employee already logged in");
            }
            employeesObs.put(agencyName, client);
        }
        return employee;
    }

    @Override
    public void signin(String agencyName, String password) throws ValidationException {
        innerService.signin(agencyName, password);
    }

    @Override
    public Iterable<Flight> getAllFlights() throws ValidationException {
        return innerService.getAllFlights();
    }

    @Override
    public Iterable<Flight> findByDestinationAndDepartureDate(String destination, String departureDate) throws ValidationException {
        return innerService.findByDestinationAndDepartureDate(destination, departureDate);
    }

    @Override
    public void buyTicket(String client, Flight flight, int noOfSeats) throws ValidationException {
        innerService.buyTicket(client, flight, noOfSeats);
        System.out.println("Observers count: " + employeesObs.size());
        employeesObs.values().forEach(obs -> obs.updatedFlight(flight));
    }

    @Override
    public void logout(Employee employee) {
        employeesObs.remove(employee.getAgencyName());
        innerService.logout(employee);
    }
}
