package ro.mpp.dto;

import ro.mpp.Flight;

public class FlightDTO extends EntityDTO {
    private String destination;
    private String departureDate;
    private String departureTime;
    private String airport;
    private Integer availableSeats;

    public FlightDTO() {}

    public FlightDTO(String destination, String departureDate, String departureTime, String airport, Integer availableSeats) {
        this.destination = destination;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.airport = airport;
        this.availableSeats = availableSeats;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getAirport() {
        return airport;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public static FlightDTO fromFlight(Flight flight) {
        var f = new FlightDTO(flight.getDestination(), flight.getDepartureDate(), flight.getDepartureTime(), flight.getAirport(), flight.getAvailableSeats());
        f.setId(flight.getId());
        return f;
    }

    public Flight toFlight() {
        var f = new Flight(getDestination(), getDepartureDate(), getDepartureTime(), getAirport(), getAvailableSeats());
        f.setId(getId());
        return f;
    }
}
