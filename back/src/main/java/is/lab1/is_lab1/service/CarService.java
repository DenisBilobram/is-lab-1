package is.lab1.is_lab1.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import is.lab1.is_lab1.controller.exception.CarWithNameAlreadtExistException;
import is.lab1.is_lab1.controller.request.CarDto;
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
    public Car createCar(Car car) throws CarWithNameAlreadtExistException {

        if (carRepository.existsByName(car.getName())) throw new CarWithNameAlreadtExistException("Car with such name already exists.");

        ObjectEvent event = new ObjectEvent(EventType.CREATE, LocalDateTime.now(), car.getIsUser());
        objectEventRepository.save(event);

        car.getObjectEvents().add(event);

        return carRepository.save(car);

    }

    public Car createCar(String name, Boolean cool, IsUser user) throws CarWithNameAlreadtExistException {
        if (name == null || cool == null) return null;

        CarDto dto = new CarDto();
        dto.setName(name);
        dto.setCool(cool);
        dto.setAdminsCanEdit(true);

        return createCar(new Car(dto, user));

    }

    @Transactional
    public boolean createAllCars(List<Car> cars) throws CarWithNameAlreadtExistException {
        for (Car car : cars) {
            createCar(car);
        }
        return true;
    }

    @PreAuthorize("@accessService.canAccessCar(#car.id)")
    @Transactional
    public Car updateCar(Car car, IsUser user) {

        ObjectEvent event = new ObjectEvent(EventType.UPDATE, LocalDateTime.now(), user);
        objectEventRepository.save(event);

        car.getObjectEvents().add(event);

        System.out.println("came to save car");
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

    @PreAuthorize("@accessService.canAccessCar(#id)")
    public Car getWithAcces(Long id) throws AccessDeniedException {
        Car car = carRepository.getReferenceById(id);

        return car;
    }

    @PreAuthorize("@accessService.canAccessCar(#car.id)")
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
