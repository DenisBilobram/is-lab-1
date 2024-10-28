package is.lab1.is_lab1.controller.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmailNotFoundException extends IsException {

    public EmailNotFoundException(String message) {
        super(message);
    }

}
