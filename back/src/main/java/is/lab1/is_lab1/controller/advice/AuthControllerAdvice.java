package is.lab1.is_lab1.controller.advice;

import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import is.lab1.is_lab1.controller.AuthController;
import is.lab1.is_lab1.controller.exception.RegistrationFailException;
import is.lab1.is_lab1.controller.exception.RootsRequestAlreadyExist;


@ControllerAdvice(assignableTypes = {AuthController.class})
public class AuthControllerAdvice {
    
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleBadCredentialsException(BadCredentialsException exp) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "There is no user with this (username, password) pair.");
        return response;
    }

    @ExceptionHandler(RegistrationFailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleRegistrationFailException(RegistrationFailException exp) {
        Map<String, String> response = new HashMap<>();
        response.put("message", exp.getMessage());
        return response;
    }


    @ExceptionHandler(RootsRequestAlreadyExist.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleRootsRequestAlreadyExist(RootsRequestAlreadyExist exp) {
        Map<String, String> response = new HashMap<>();
        response.put("message", exp.getMessage());
        return response;
    }
}
