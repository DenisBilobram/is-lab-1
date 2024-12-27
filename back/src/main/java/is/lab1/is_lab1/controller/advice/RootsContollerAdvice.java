package is.lab1.is_lab1.controller.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import is.lab1.is_lab1.controller.RootsController;
import is.lab1.is_lab1.controller.exception.IsException;

@ControllerAdvice(assignableTypes = {RootsController.class})
public class RootsContollerAdvice {

    @ExceptionHandler(IsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleIsException(IsException exp) {
        Map<String, String> response = new HashMap<>();
        response.put("message", exp.getMessage());
        return response;
    }
    
}
