package scrapper.db.dao.exception;

public class IncompleteFlightDataException extends RuntimeException {

    public IncompleteFlightDataException(String message) {
        super(message);
    }

    public IncompleteFlightDataException(String message, Throwable cause) {
        super(message, cause);
    }
}