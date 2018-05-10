package data.manager.model.dao.exception;

public class AirportDataException extends RuntimeException {

    public AirportDataException(String message) {
        super(message);
    }

    public AirportDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
