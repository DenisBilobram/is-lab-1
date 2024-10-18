package is.lab1.is_lab1.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import is.lab1.is_lab1.controller.AuthController;
import is.lab1.is_lab1.controller.exception.RegistrationFailException;


@ControllerAdvice(assignableTypes = {AuthController.class})
public class AuthControllerAdvice {
    
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleBadCredentialsException(BadCredentialsException exp) {
        return "This no user witch this (username, password) pair.";
    }

    @ExceptionHandler(RegistrationFailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleRegistrationFailException(RegistrationFailException exp) {
        return exp.getMessage();
    }

}
