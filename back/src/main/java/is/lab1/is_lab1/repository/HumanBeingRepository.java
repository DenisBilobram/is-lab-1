package is.lab1.is_lab1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import is.lab1.is_lab1.model.Car;
import is.lab1.is_lab1.model.Coordinates;
import is.lab1.is_lab1.model.HumanBeing;

public interface HumanBeingRepository extends JpaRepository<HumanBeing, Long> {
    
    public List<HumanBeing> findByCoordinates(Coordinates coordinates);

    List<HumanBeing> findByCar(Car car);

}
