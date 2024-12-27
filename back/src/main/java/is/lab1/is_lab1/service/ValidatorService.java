package is.lab1.is_lab1.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;

@Service
public class ValidatorService {
    
    @Autowired
    private Validator validator;

    public <T> void validateObejct(T object) throws ValidationException {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations.iterator().next().getMessage());
        }
    }

    public <T> void validateObjects(List<T> objects) throws ValidationException {
        for (T obj : objects) {
            validateObejct(obj);
        }
    }

}
