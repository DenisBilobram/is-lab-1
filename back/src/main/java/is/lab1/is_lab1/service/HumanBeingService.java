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
import is.lab1.is_lab1.model.HumanBeing;
import is.lab1.is_lab1.model.IsUser;
import is.lab1.is_lab1.model.ObjectEvent;
import is.lab1.is_lab1.repository.HumanBeingRepository;
import is.lab1.is_lab1.repository.ObjectEventRepository;
import jakarta.transaction.Transactional;

@Service
public class HumanBeingService {
    
    @Autowired
    HumanBeingRepository humanBeingRepository;

    @Autowired
    ObjectEventRepository objectEventRepository;

    @Transactional
    public HumanBeing createHumanBeing(HumanBeing humanBeing) {

        ObjectEvent event = new ObjectEvent(EventType.CREATE, LocalDateTime.now(), humanBeing.getIsUser());
        objectEventRepository.save(event);

        humanBeing.getObjectEvents().add(event);

        return humanBeingRepository.save(humanBeing);
    }

    @Transactional
    public HumanBeing updateHumanBeing(HumanBeing humanBeing, IsUser user) {

        ObjectEvent event = new ObjectEvent(EventType.UPDATE, LocalDateTime.now(), user);
        objectEventRepository.save(event);

        humanBeing.getObjectEvents().add(event);

        return humanBeingRepository.save(humanBeing);

    }

    public List<HumanBeing> getAll(Integer size, Integer page) {
        if (size != null && page != null) {
            Pageable pageable = PageRequest.of(page, size);
            return humanBeingRepository.findAll(pageable).getContent();
        } else {
            return humanBeingRepository.findAll();
        }
    }

    public HumanBeing getWithAcces(Long id, IsUser user) throws AccessDeniedException {
        HumanBeing humanBeing = humanBeingRepository.getReferenceById(id);

        if (!humanBeing.getIsUser().equals(user)) throw new AccessDeniedException("No acces to this object.");

        return humanBeing;
    }

    public void delete(HumanBeing humanBeing) {

        humanBeingRepository.delete(humanBeing);

    }

    public void deleteAll(List<HumanBeing> humanBeings) {

        humanBeingRepository.deleteAll(humanBeings);

    }

    public List<HumanBeing> getByCoordinates(Coordinates coordinates) {

        return humanBeingRepository.findByCoordinates(coordinates);

    }

}
