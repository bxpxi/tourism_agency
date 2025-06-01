package ro.mpp.exceptions;

public class ServerProcessingException extends Throwable {
    public ServerProcessingException(Exception e) {
        super(e);
    }
}
