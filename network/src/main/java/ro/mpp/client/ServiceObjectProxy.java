package ro.mpp.client;

import ro.mpp.Employee;
import ro.mpp.Flight;
import ro.mpp.IAppService;
import ro.mpp.ValidationException;
import ro.mpp.dto.DTOFactory;
import ro.mpp.dto.TicketDTO;
import ro.mpp.objectprotocol.*;
import ro.mpp.observer.Observer;

import java.util.Arrays;

public class ServiceObjectProxy extends ServiceObjectProxyBase implements IAppService {
    public ServiceObjectProxy(String host, int port) {
        super(host, port);
    }

    Observer client;

    @Override
    public Employee login(String agencyName, String password, Observer client) throws ValidationException {
        initializeConnection();
        sendRequest(new LoginEmployeeRequest(agencyName, password));
        var response = readResponse();
        this.client = client;

        Employee employee = null;
        if(response instanceof LoginEmployeeResponse) {
            System.out.println("Login successful");
            employee = DTOFactory.fromDTO(((LoginEmployeeResponse)response).getEmployee());
        } else if(response instanceof ErrorResponse) {
            closeConnection();
            throw new ValidationException("Login failed");
        } else {
            System.out.println("Login ???");
            closeConnection();
            throw new ValidationException("Login failed");
        }

        return employee;
    }

    @Override
    public void signin(String agencyName, String password) throws ValidationException {

    }

    @Override
    public Iterable<Flight> getAllFlights() throws ValidationException {
        testConnectionOpen();
        sendRequest(new GetAllFlightsRequest());
        var response = readResponse();
        Iterable<Flight> flights = null;
        if(response instanceof GetAllFlightsResponse) {
            flights = () -> Arrays.stream(DTOFactory.fromDTO(((GetAllFlightsResponse) response).getFlights())).iterator();
        } else {
            throw new ValidationException("Received: " + response.getClass().getName());
        }
        return flights;
    }

    @Override
    public Iterable<Flight> findByDestinationAndDepartureDate(String destination, String departureDate) throws ValidationException {
        testConnectionOpen();
        sendRequest(new FilterFlightsRequest(destination, departureDate));
        var response = readResponse();
        Iterable<Flight> flights = null;
        if(response instanceof FilterFlightsResponse) {
            flights = () -> Arrays.stream(DTOFactory.fromDTO(((FilterFlightsResponse) response).getFlights())).iterator();
        } else {
            throw new ValidationException("Received: " + response.getClass().getName());
        }
        return flights;
    }

    @Override
    public void buyTicket(String client, Flight flight, int noOfSeats) throws ValidationException {
        testConnectionOpen();
        var ticket = new TicketDTO(client, DTOFactory.getDTO(flight), noOfSeats);
        sendRequest(new BuyTicketRequest(ticket));
        var response = readResponse();
        if(!(response instanceof BuyTicketResponse)) {
            throw new ValidationException("Received: " + response);
        }
    }

    @Override
    public void logout(Employee employee) {
        closeConnection();
    }

    @Override
    protected void handleUpdate(UpdateResponse update) {
        if(update instanceof UpdatedFlightResponse) {
            var flight = ((UpdatedFlightResponse)update).getFlight();
            System.out.println("Updated flight: " + flight);
            try {
                client.updatedFlight(DTOFactory.fromDTO(flight));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
