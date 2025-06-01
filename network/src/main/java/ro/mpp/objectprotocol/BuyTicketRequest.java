package ro.mpp.objectprotocol;

import ro.mpp.dto.FlightDTO;
import ro.mpp.dto.TicketDTO;

public class BuyTicketRequest implements IRequest {
    TicketDTO ticket;

    public BuyTicketRequest() {}

    public BuyTicketRequest(TicketDTO ticket) {
        this.ticket = ticket;
    }

    public TicketDTO getTicket() {
        return this.ticket;
    }
}
