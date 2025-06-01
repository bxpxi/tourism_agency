package ro.mpp.dto;

import ro.mpp.Flight;

public class TicketDTO extends EntityDTO {
    private String client;
    private FlightDTO flight;
    private Integer noOfSeats;

    public TicketDTO() {}

    public TicketDTO(final String client, final FlightDTO flight, final Integer noOfSeats) {
        this.client = client;
        this.flight = flight;
        this.noOfSeats = noOfSeats;
    }

    public String getClient() {
        return client;
    }

    public FlightDTO getFlight() {
        return flight;
    }

    public Integer getNoOfSeats() {
        return noOfSeats;
    }
}
