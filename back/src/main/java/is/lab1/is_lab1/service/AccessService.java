package is.lab1.is_lab1.service;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import is.lab1.is_lab1.model.Ownable;
import is.lab1.is_lab1.repository.CarRepository;
import is.lab1.is_lab1.repository.CoordinatesRepository;
import is.lab1.is_lab1.repository.HumanBeingRepository;

@Service("accessService")
public class AccessService {
    
    @Autowired
    CarRepository carRepository;

    @Autowired
    CoordinatesRepository coordinatesRepository;

    @Autowired
    HumanBeingRepository humanBeingRepository;

    public boolean canAccessCar(Long carId) {
        return canAccess(carId, carRepository::findById);
    }

    public boolean canAccessHumanBeing(Long humanBeingId) {
        return canAccess(humanBeingId, humanBeingRepository::findById);
    }

    public boolean canAccessCoordinates(Long coordinatesId) {
        return canAccess(coordinatesId, coordinatesRepository::findById);
    }

    private <T extends Ownable> boolean canAccess(Long id, Function<Long, Optional<T>> fetchFunction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        boolean hasAdminRole = authentication.getAuthorities().stream()
                                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        Optional<T> optionalEntity = fetchFunction.apply(id);
        if (optionalEntity.isEmpty()) {
            return false;
        }
        T entity = optionalEntity.get();
        return (entity.getIsUser().getUsername().equals(currentUsername)) ||
               (hasAdminRole && entity.isAdminsCanEdit());
    }
}