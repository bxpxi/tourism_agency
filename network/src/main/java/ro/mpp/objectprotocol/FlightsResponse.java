package ro.mpp.objectprotocol;

import ro.mpp.Flight;
import ro.mpp.dto.DTOFactory;
import ro.mpp.dto.FlightDTO;

import java.util.Arrays;
import java.util.List;

public class FlightsResponse implements IResponse {
    private final FlightDTO[] flights;

    protected FlightsResponse(Flight[] flights) {
        this.flights = DTOFactory.getDTO(Arrays.asList(flights));
    }

    public List<FlightDTO> getFlights() {
        return Arrays.asList(flights);
    }
}
