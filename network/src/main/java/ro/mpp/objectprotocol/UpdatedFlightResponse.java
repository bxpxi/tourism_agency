package ro.mpp.objectprotocol;

import ro.mpp.Flight;
import ro.mpp.dto.FlightDTO;

public class UpdatedFlightResponse extends UpdateResponse{
    FlightDTO flight;

    public UpdatedFlightResponse(FlightDTO flight) {
        this.flight = flight;
    }

    public FlightDTO getFlight() {
        return flight;
    }
}
