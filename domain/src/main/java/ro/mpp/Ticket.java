package ro.mpp;

import java.util.Objects;

public class Ticket implements Entity<Integer> {
    private int id;
    private String client;
    private Flight flight;
    private Integer noOfSeats;

    public Ticket(String client, Flight flight, Integer noOfSeats) {
        this.client = client;
        this.flight = flight;
        this.noOfSeats = noOfSeats;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Integer getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(Integer noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    @Override
    public String toString() {
        return "Ticket [id=" + id + ", client=" + client + ", flight=" + flight + ", noOfSeats=" + noOfSeats + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && Objects.equals(client, ticket.client) && Objects.equals(flight, ticket.flight) && Objects.equals(noOfSeats, ticket.noOfSeats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, flight, noOfSeats);
    }
}
