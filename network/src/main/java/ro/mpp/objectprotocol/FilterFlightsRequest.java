package ro.mpp.objectprotocol;

import java.io.Serializable;

public class FilterFlightsRequest implements IRequest {
    String destination;
    String date;

    public FilterFlightsRequest(String destination, String date) {
        this.destination = destination;
        this.date = date;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }
}
