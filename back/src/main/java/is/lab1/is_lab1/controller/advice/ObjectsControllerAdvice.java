package is.lab1.is_lab1.controller.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import is.lab1.is_lab1.controller.ObjectsController;
import is.lab1.is_lab1.controller.exception.IsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;

@ControllerAdvice(assignableTypes = {ObjectsController.class})
public class ObjectsControllerAdvice {
    
    @ExceptionHandler(IsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleIsException(IsException exp) {
        Map<String, String> response = new HashMap<>();
        response.put("message", exp.getMessage());
        return response;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleNotFoundException(EntityNotFoundException exp) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "There is no object with this ID.");
        return response;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleValidationException(ValidationException exp) {
        Map<String, String> response = new HashMap<>();
        response.put("message", exp.getMessage());
        return response;
    }
}
