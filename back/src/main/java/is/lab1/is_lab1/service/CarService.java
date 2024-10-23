package is.lab1.is_lab1.service;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import is.lab1.is_lab1.model.Car;
import is.lab1.is_lab1.model.EventType;
import is.lab1.is_lab1.model.HumanBeing;
import is.lab1.is_lab1.model.IsUser;
import is.lab1.is_lab1.model.ObjectEvent;
import is.lab1.is_lab1.repository.CarRepository;
import is.lab1.is_lab1.repository.HumanBeingRepository;
import is.lab1.is_lab1.repository.ObjectEventRepository;
import jakarta.transaction.Transactional;

@Service
public class CarService {
    
    @Autowired
    CarRepository carRepository;

    @Autowired
    ObjectEventRepository objectEventRepository;

    @Autowired
    HumanBeingRepository humanBeingRepository;

    public Car get(Long id) {

        return carRepository.getReferenceById(id);

    }

    @Transactional
    public Car createCar(Car car) {

        ObjectEvent event = new ObjectEvent(EventType.CREATE, LocalDateTime.now(), car.getIsUser());
        objectEventRepository.save(event);

        car.getObjectEvents().add(event);

        return carRepository.save(car);

    }

    @Transactional
    public Car updateCar(Car car, IsUser user) {

        ObjectEvent event = new ObjectEvent(EventType.UPDATE, LocalDateTime.now(), user);
        objectEventRepository.save(event);

        car.getObjectEvents().add(event);

        return carRepository.save(car);

    }

    public List<Car> getAll(Integer size, Integer page) {
        if (size != null && page != null) {
            Pageable pageable = PageRequest.of(page, size);
            return carRepository.findAll(pageable).getContent();
        } else {
            return carRepository.findAll();
        }
    }

    public Car getWithAcces(Long id, IsUser user) throws AccessDeniedException {
        Car car = carRepository.getReferenceById(id);

        if (!car.getIsUser().equals(user)) throw new AccessDeniedException("No acces to this object.");

        return car;
    }


    @Transactional
    public void delete(Car car) {

        List<HumanBeing> humanBeings = humanBeingRepository.findByCar(car);

        for (HumanBeing humanBeing : humanBeings) {
            humanBeing.setCar(null);
            humanBeingRepository.save(humanBeing);
        }

        carRepository.delete(car);

    }
}
