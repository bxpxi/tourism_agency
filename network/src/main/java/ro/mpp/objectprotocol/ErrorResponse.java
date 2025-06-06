package ro.mpp.objectprotocol;

public class ErrorResponse implements IResponse {
    private final String message;

    public ErrorResponse() { this.message = ""; }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(Exception e) {
        this.message = e.toString();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ErrorResponse [message=" + message + "]";
    }
}
