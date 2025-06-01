package ro.mpp;

import java.util.Objects;

public class Flight implements Entity<Integer> {
    private int id;
    private String destination;
    private String departureDate;
    private String departureTime;
    private String airport;
    private Integer availableSeats;

    public Flight(String destination, String departureDate, String departureTime, String airport, Integer availableSeats) {
        this.destination = destination;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.airport = airport;
        this.availableSeats = availableSeats;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "Flight [id=" + id + ", destination=" + destination + ", departureDate=" + departureDate + ", departureTime=" + departureTime + ", airport=" + airport + ", availableSeats=" + availableSeats + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(id, flight.id) && Objects.equals(destination, flight.destination) && Objects.equals(departureDate, flight.departureDate) && Objects.equals(departureTime, flight.departureTime) && Objects.equals(airport, flight.airport) && Objects.equals(availableSeats, flight.availableSeats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, destination, departureDate, departureTime, airport, availableSeats);
    }
}
