package is.lab1.is_lab1.controller.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RootsRequestAlreadyExistException extends IsException {
    public RootsRequestAlreadyExistException(String message) {
        super(message);
    }
}
