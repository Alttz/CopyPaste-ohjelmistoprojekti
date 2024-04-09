package copypaste.ticketguru.domain;

public class RESTError {
    private String errorDescription;
    public RESTError(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
