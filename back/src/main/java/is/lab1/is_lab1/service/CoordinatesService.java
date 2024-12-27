package is.lab1.is_lab1.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import is.lab1.is_lab1.controller.exception.CoordinatesAlreadyExistException;
import is.lab1.is_lab1.controller.request.CoordinatesDto;
import is.lab1.is_lab1.model.Coordinates;
import is.lab1.is_lab1.model.EventType;
import is.lab1.is_lab1.model.IsUser;
import is.lab1.is_lab1.model.ObjectEvent;
import is.lab1.is_lab1.repository.CoordinatesRepository;
import is.lab1.is_lab1.repository.HumanBeingRepository;
import is.lab1.is_lab1.repository.ObjectEventRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

@Service
public class CoordinatesService {

    @Autowired
    CoordinatesRepository coordinatesRepository;

    @Autowired
    ObjectEventRepository objectEventRepository;

    @Autowired
    HumanBeingRepository humanBeingRepository;

    @Autowired
    ValidatorService validatorService;

    public Coordinates get(Long id) {

        return coordinatesRepository.getReferenceById(id);

    }

    @Transactional
    public Coordinates createCoordinates(Coordinates coordinates) throws CoordinatesAlreadyExistException {

        if (coordinatesRepository.existsByXAndY(coordinates.getX(), coordinates.getY())) throw new CoordinatesAlreadyExistException("Coordinates (x,y) already exists.");

        ObjectEvent event = new ObjectEvent(EventType.CREATE, LocalDateTime.now(), coordinates.getIsUser());
        objectEventRepository.save(event);

        coordinates.getObjectEvents().add(event);

        return coordinatesRepository.save(coordinates);

    }

    public Coordinates createCoordinates(Double x, float y, IsUser isUser) throws ValidationException, CoordinatesAlreadyExistException {

        CoordinatesDto dto = new CoordinatesDto();
        dto.setX(x);
        dto.setY(y);
        dto.setAdminsCanEdit(true);
        validatorService.validateObejct(dto);

        return createCoordinates(new Coordinates(dto, isUser));

    }

    @PreAuthorize("@accessService.canAccessCoordinates(#coordinates.id)")
    @Transactional
    public Coordinates updateCoordinates(Coordinates coordinates, IsUser user) {

        ObjectEvent event = new ObjectEvent(EventType.UPDATE, LocalDateTime.now(), user);
        objectEventRepository.save(event);

        coordinates.getObjectEvents().add(event);

        return coordinatesRepository.save(coordinates);

    }

    public List<Coordinates> getAll(Integer size, Integer page) {
        if (size != null && page != null) {
            Pageable pageable = PageRequest.of(page, size);
            return coordinatesRepository.findAll(pageable).getContent();
        } else {
            return coordinatesRepository.findAll();
        }
    }

    @PreAuthorize("@accessService.canAccessCoordinates(#id)")
    public Coordinates getWithAcces(Long id, IsUser user) throws AccessDeniedException {

        Coordinates coordinates = coordinatesRepository.getReferenceById(id);
        return coordinates;
        
    }

    @PreAuthorize("@accessService.canAccessCoordinates(#coordinates.id)")
    @Transactional
    public void delete(Coordinates coordinates) {

        humanBeingRepository.deleteAll(humanBeingRepository.findByCoordinates(coordinates));

        coordinatesRepository.delete(coordinates);

    }
}
