package is.lab1.is_lab1.service;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import is.lab1.is_lab1.model.Coordinates;
import is.lab1.is_lab1.model.EventType;
import is.lab1.is_lab1.model.IsUser;
import is.lab1.is_lab1.model.ObjectEvent;
import is.lab1.is_lab1.repository.CoordinatesRepository;
import is.lab1.is_lab1.repository.HumanBeingRepository;
import is.lab1.is_lab1.repository.ObjectEventRepository;
import jakarta.transaction.Transactional;

@Service
public class CoordinatesService {

    @Autowired
    CoordinatesRepository coordinatesRepository;

    @Autowired
    ObjectEventRepository objectEventRepository;

    @Autowired
    HumanBeingRepository humanBeingRepository;

    public Coordinates get(Long id) {

        return coordinatesRepository.getReferenceById(id);

    }

    @Transactional
    public Coordinates createCoordinates(Coordinates coordinates) {

        ObjectEvent event = new ObjectEvent(EventType.CREATE, LocalDateTime.now(), coordinates.getIsUser());
        objectEventRepository.save(event);

        coordinates.getObjectEvents().add(event);

        return coordinatesRepository.save(coordinates);

    }

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

    public Coordinates getWithAcces(Long id, IsUser user) throws AccessDeniedException {
        Coordinates coordinates = coordinatesRepository.getReferenceById(id);

        if (!coordinates.getIsUser().equals(user)) throw new AccessDeniedException("No acces to this object.");

        return coordinates;
    }

    @Transactional
    public void delete(Coordinates coordinates) {

        humanBeingRepository.deleteAll(humanBeingRepository.findByCoordinates(coordinates));

        coordinatesRepository.delete(coordinates);

    }
}
