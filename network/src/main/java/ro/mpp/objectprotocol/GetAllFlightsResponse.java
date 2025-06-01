package ro.mpp.objectprotocol;

import ro.mpp.Flight;

public class GetAllFlightsResponse extends FlightsResponse{
    public GetAllFlightsResponse(Flight[] flights) {
        super(flights);
    }
}
