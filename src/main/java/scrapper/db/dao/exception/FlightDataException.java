package scrapper.db.dao.exception;

public class FlightDataException extends RuntimeException {

    public FlightDataException(String message) {
        super(message);
    }

    public FlightDataException(String message, Throwable cause) {
        super(message, cause);
    }
}