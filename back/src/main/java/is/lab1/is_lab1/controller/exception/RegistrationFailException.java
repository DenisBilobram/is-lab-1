package is.lab1.is_lab1.controller.exception;

public class RegistrationFailException extends Exception {

    private String message;

    public RegistrationFailException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
