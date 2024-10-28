package is.lab1.is_lab1.controller.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidResetTokenException extends IsException {
    public InvalidResetTokenException(String message) {
        super(message);
    }
}
