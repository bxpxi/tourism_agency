package ro.mpp;

import ro.mpp.databases.EmployeeRepository;
import ro.mpp.databases.FlightRepository;
import ro.mpp.databases.TicketRepository;
import ro.mpp.server.ObjectConcurrentServer;
import ro.mpp.server.ServerException;
import ro.mpp.server.ServiceImplementation;
import ro.mpp.services.EmployeeService;
import ro.mpp.services.FlightService;
import ro.mpp.services.TicketService;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws ServerException, IOException {
        var props = loadProperties();
        var employeeRepo = new EmployeeRepository(props);
        var flightRepo = new FlightRepository(props);
        var ticketRepo = new TicketRepository(props, flightRepo);

        var employeeService = new EmployeeService(employeeRepo);
        var flightService = new FlightService(flightRepo);
        var ticketService = new TicketService(ticketRepo);

        var appService = new AppService(employeeService, flightService, ticketService);
        var server = new ObjectConcurrentServer(15000, new ServiceImplementation(appService));
        server.start();
    }

    public static Properties loadProperties() {
        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.properties"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.properties" + e);
        }
        return props;
    }
}