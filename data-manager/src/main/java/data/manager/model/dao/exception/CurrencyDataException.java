package data.manager.model.dao.exception;

public class CurrencyDataException extends RuntimeException {

    public CurrencyDataException(String message) {
        super(message);
    }

    public CurrencyDataException(String message, Throwable cause) {
        super(message, cause);
    }
}