package ro.mpp.objectprotocol;

import ro.mpp.Flight;

public class FilterFlightsResponse extends FlightsResponse {
    public FilterFlightsResponse(Flight[] flights) {
        super(flights);
    }
}
