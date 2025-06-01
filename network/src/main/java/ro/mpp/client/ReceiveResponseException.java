package ro.mpp.client;

import ro.mpp.ValidationException;

public class ReceiveResponseException extends ValidationException {
    public ReceiveResponseException(String message) {
        super(message);
    }
}
