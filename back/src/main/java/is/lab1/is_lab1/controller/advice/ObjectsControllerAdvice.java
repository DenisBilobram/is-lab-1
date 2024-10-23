package is.lab1.is_lab1.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import is.lab1.is_lab1.controller.ObjectsController;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;

@ControllerAdvice(assignableTypes = {ObjectsController.class})
public class ObjectsControllerAdvice {
    
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleValidationException(ValidationException exp) {
        return exp.getMessage();
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String handleAccesException(AccessDeniedException exp) {
        return exp.getMessage();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleNotFoundException(EntityNotFoundException exp) {
        return "There is no object with this ID.";
    }
}
