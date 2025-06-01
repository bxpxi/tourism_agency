package ro.mpp.dto;

import ro.mpp.Employee;
import ro.mpp.Flight;
import ro.mpp.Ticket;

import java.util.stream.StreamSupport;

public class DTOFactory {
    public static EmployeeDTO getDTO(Employee emp) {
        return EmployeeDTO.fromEmployee(emp);
    }

    public static FlightDTO getDTO(Flight fl) {
        return FlightDTO.fromFlight(fl);
    }

    public static FlightDTO[] getDTO(Iterable<Flight> flights) {
        return StreamSupport.stream(flights.spliterator(), false).map(DTOFactory::getDTO).toArray(FlightDTO[]::new);
    }

    public static Flight fromDTO(FlightDTO f) {
        return f.toFlight();
    }

    public static Employee fromDTO(EmployeeDTO e) {
        return e.toEmployee();
    }

    public static Flight[] fromDTO(Iterable<FlightDTO> flights) {
        return StreamSupport.stream(flights.spliterator(), false).map(DTOFactory::fromDTO).toArray(Flight[]::new);
    }
}
