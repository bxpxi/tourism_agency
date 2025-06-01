package ro.mpp.client;

import ro.mpp.ValidationException;

public class SendRequestException extends ValidationException {
    public SendRequestException(String msg){
        super(msg);
    }
}
