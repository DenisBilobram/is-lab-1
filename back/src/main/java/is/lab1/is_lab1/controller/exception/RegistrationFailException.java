package is.lab1.is_lab1.controller.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RegistrationFailException extends IsException {
    public RegistrationFailException(String message) {
        super(message);
    }
}
