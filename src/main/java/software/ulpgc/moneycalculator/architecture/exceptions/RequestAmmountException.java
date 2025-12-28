package software.ulpgc.moneycalculator.architecture.exceptions;

public class RequestAmmountException extends Exception {
    public RequestAmmountException(String errorMessage) {
        super(errorMessage);
    }
}
