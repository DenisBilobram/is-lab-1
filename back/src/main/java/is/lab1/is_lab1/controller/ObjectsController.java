package is.lab1.is_lab1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import is.lab1.is_lab1.controller.request.CarDto;
import is.lab1.is_lab1.controller.request.CoordinatesDto;
import is.lab1.is_lab1.controller.request.HumanBeingDto;
import is.lab1.is_lab1.controller.request.ObjectOperationType;
import is.lab1.is_lab1.model.Car;
import is.lab1.is_lab1.model.Coordinates;
import is.lab1.is_lab1.model.HumanBeing;
import is.lab1.is_lab1.model.Mood;
import is.lab1.is_lab1.model.WeaponType;
import is.lab1.is_lab1.service.CarService;
import is.lab1.is_lab1.service.CoordinatesService;
import is.lab1.is_lab1.service.HumanBeingService;
import is.lab1.is_lab1.service.IsUserDetails;
import is.lab1.is_lab1.service.ObjectsImportService;
import is.lab1.is_lab1.service.WebSocketService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("api/objects")
public class ObjectsController {

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private HumanBeingService humanBeingService;

    @Autowired
    private CoordinatesService coordinatesService;

    @Autowired
    private CarService carService;

    @Autowired
    private ObjectsImportService importService;
    

    
    @SuppressWarnings("null")
    @PostMapping("human-being")
    public ResponseEntity<?> humanCreate(@Valid @RequestBody HumanBeingDto humanBeingDto,
                                BindingResult bindingResult, @AuthenticationPrincipal IsUserDetails userDetails) {
        
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldError().getDefaultMessage());
        }
        HumanBeing humanBeing = new HumanBeing(humanBeingDto, userDetails.getIsUser(), coordinatesService.get(humanBeingDto.getCoordinates()));

        if (humanBeingDto.getCar() != null) {
            humanBeing.setCar(carService.get(humanBeingDto.getCar()));
        }
        
        humanBeing = humanBeingService.createHumanBeing(humanBeing);

        HumanBeingDto object = new HumanBeingDto(humanBeing);
        object.setType(ObjectOperationType.CREATE);

        webSocketService.notifySubscribers("human-being", object);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("human-being")
    public ResponseEntity<List<HumanBeingDto>> getHumanBeingsList(@RequestParam(required = false) Integer size, @RequestParam(required = false) Integer page) {
        
        List<HumanBeing> humanBeingsList = humanBeingService.getAll(size, page);
        
        List<HumanBeingDto> humanBeingsDtoList = humanBeingsList.stream().map(HumanBeingDto::new).collect(Collectors.toList());

        return new ResponseEntity<>(humanBeingsDtoList, HttpStatus.OK);
    }

    @GetMapping("human-being/{id}")
    public ResponseEntity<HumanBeingDto> getHumanBeing(@PathVariable Long id, @AuthenticationPrincipal IsUserDetails userDetails) throws AccessDeniedException {

        HumanBeing humanBeing = humanBeingService.getWithAcces(id, userDetails.getIsUser());

        return new ResponseEntity<>(new HumanBeingDto(humanBeing), HttpStatus.OK);

    }

    @PutMapping("human-being/{id}")
    public ResponseEntity<?> updateHumanBeing(@PathVariable Long id, @Valid @RequestBody HumanBeingDto humanBeingDto,
                                                @AuthenticationPrincipal IsUserDetails userDetails) throws AccessDeniedException {
        
        HumanBeing humanBeing = humanBeingService.getWithAcces(id, userDetails.getIsUser());

        humanBeing.setDtoData(humanBeingDto);
        if (humanBeingDto.getCar() != null) {
            humanBeing.setCar(carService.get(humanBeingDto.getCar()));
        }
        humanBeing.setCoordinates(coordinatesService.get(humanBeingDto.getCoordinates()));

        humanBeing = humanBeingService.updateHumanBeing(humanBeing, userDetails.getIsUser());

        HumanBeingDto object = new HumanBeingDto(humanBeing);
        object.setType(ObjectOperationType.UPDATE);
        webSocketService.notifySubscribers("human-being", object);
        
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("human-being/{id}")
    public ResponseEntity<?> deleteHumanBeing(@PathVariable Long id, @AuthenticationPrincipal IsUserDetails userDetails) throws AccessDeniedException {

        HumanBeing humanBeing = humanBeingService.getWithAcces(id, userDetails.getIsUser());
        
        humanBeingService.delete(humanBeing);

        HumanBeingDto object = new HumanBeingDto();
        object.setType(ObjectOperationType.DELETE);
        object.setId(id);
        webSocketService.notifySubscribers("human-being", object);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @SuppressWarnings("null")
    @PostMapping("coordinates")
    public ResponseEntity<?> coordinatesCreate(@Valid @RequestBody CoordinatesDto coordinatesDto,
                                                BindingResult bindingResult, @AuthenticationPrincipal IsUserDetails userDetails) {
        
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldError().getDefaultMessage());
        }

        Coordinates coordinates = new Coordinates(coordinatesDto, userDetails.getIsUser());
        coordinates = coordinatesService.createCoordinates(coordinates);

        CoordinatesDto object = new CoordinatesDto(coordinates);
        object.setType(ObjectOperationType.CREATE);

        webSocketService.notifySubscribers("coordinates", object);
        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("coordinates")
    public ResponseEntity<List<CoordinatesDto>> getCoordinatesList(@RequestParam(required = false) Integer size, @RequestParam(required = false) Integer page) {

        List<Coordinates> coordsList = coordinatesService.getAll(size, page);
        List<CoordinatesDto> coordsDtoList = coordsList.stream().map(CoordinatesDto::new).collect(Collectors.toList());

        return new ResponseEntity<>(coordsDtoList, HttpStatus.OK);
    }

    @GetMapping("coordinates/{id}")
    public ResponseEntity<CoordinatesDto> getCoordinates(@PathVariable Long id, @AuthenticationPrincipal IsUserDetails userDetails) throws AccessDeniedException {
        Coordinates coordinates = coordinatesService.getWithAcces(id, userDetails.getIsUser());

        return new ResponseEntity<>(new CoordinatesDto(coordinates), HttpStatus.OK);
    }

    @PutMapping("coordinates/{id}")
    public ResponseEntity<?> updateCoordinates(@PathVariable Long id, @Valid @RequestBody CoordinatesDto coordinatesDto,
                                                @AuthenticationPrincipal IsUserDetails userDetails) throws AccessDeniedException {
        
        Coordinates coordinates = coordinatesService.getWithAcces(id, userDetails.getIsUser());

        coordinates.setDtoData(coordinatesDto);
        coordinates = coordinatesService.updateCoordinates(coordinates, userDetails.getIsUser());

        CoordinatesDto object = new CoordinatesDto(coordinates);
        object.setType(ObjectOperationType.UPDATE);

        webSocketService.notifySubscribers("coordinates", object);
        
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("coordinates/{id}")
    public ResponseEntity<?> deleteCoordinates(@PathVariable Long id, @AuthenticationPrincipal IsUserDetails userDetails) throws AccessDeniedException {

        Coordinates coordinates = coordinatesService.getWithAcces(id, userDetails.getIsUser());
        
        coordinatesService.delete(coordinates);

        CoordinatesDto object = new CoordinatesDto();
        object.setType(ObjectOperationType.DELETE);
        object.setId(id);

        webSocketService.notifySubscribers("coordinates", object);
        
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    

    @SuppressWarnings("null")
    @PostMapping("car")
    public ResponseEntity<?> coordinatesCreate(@Valid @RequestBody CarDto carDto,
                                                BindingResult bindingResult, @AuthenticationPrincipal IsUserDetails userDetails) {
        
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldError().getDefaultMessage());
        }

        Car car = new Car(carDto, userDetails.getIsUser());
        car = carService.createCar(car);
        
        CarDto object = new CarDto(car);
        object.setType(ObjectOperationType.CREATE);
        
        webSocketService.notifySubscribers("car", object);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("car")
    public ResponseEntity<List<CarDto>> getCarsList(@RequestParam(required = false) Integer size, @RequestParam(required = false) Integer page) {
        
        List<Car> carsList = carService.getAll(size, page);
        
        List<CarDto> carDtoList = carsList.stream().map(CarDto::new).collect(Collectors.toList());

        return new ResponseEntity<>(carDtoList, HttpStatus.OK);
    }

    @GetMapping("car/{id}")
    public ResponseEntity<CarDto> getCar(@PathVariable Long id) throws AccessDeniedException {

        Car car = carService.getWithAcces(id);

        return new ResponseEntity<>(new CarDto(car), HttpStatus.OK);
    }

    @PutMapping("car/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @Valid @RequestBody CarDto carDto,
                                                @AuthenticationPrincipal IsUserDetails userDetails) throws AccessDeniedException {
        
        Car car = carService.getWithAcces(id);

        car.setDtoData(carDto);
        car = carService.updateCar(car, userDetails.getIsUser());

        CarDto object = new CarDto(car);
        object.setType(ObjectOperationType.UPDATE);
        
        webSocketService.notifySubscribers("car", object);
        
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("car/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id, @AuthenticationPrincipal IsUserDetails userDetails) throws AccessDeniedException {

        Car car = carService.getWithAcces(id);
        
        carService.delete(car);

        CarDto object = new CarDto();
        object.setType(ObjectOperationType.DELETE);
        object.setId(id);
        
        webSocketService.notifySubscribers("car", object);
        
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("human-being/count/impact-speed/{impactSpeed}")
    public ResponseEntity<Long> countByImpactSpeed(@PathVariable int impactSpeed) {
        long count = humanBeingService.countByImpactSpeed(impactSpeed);
        return ResponseEntity.ok(count);
    }

    @GetMapping("human-being/search/name")
    public ResponseEntity<List<HumanBeingDto>> findByNameContaining(@RequestParam String substring) {
        List<HumanBeingDto> results = humanBeingService.findByNameContaining(substring).stream().map(HumanBeingDto::new).toList();
        return ResponseEntity.ok(results);
    }

    @GetMapping("human-being/search/weapon-type")
    public ResponseEntity<List<HumanBeingDto>> findByWeaponTypeGreaterThan(@RequestParam WeaponType weaponType) {
        List<HumanBeingDto> results = humanBeingService.findByWeaponTypeGreaterThan(weaponType).stream().map(HumanBeingDto::new).toList();
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("human-being/delete/without-toothpick")
    public ResponseEntity<?> deleteAllWithoutToothpick() {
        humanBeingService.deleteAllHeroesWithoutToothpick();
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("human-being/update/mood-sorrow")
    public ResponseEntity<?> updateMoodForAll() {
        humanBeingService.updateMoodForAll(Mood.SORROW);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Transactional
    @PostMapping("human-being/import")
    public ResponseEntity<?> jsonImportHumanBeings(@RequestParam String jsonObjects, @AuthenticationPrincipal IsUserDetails userDetails)
                                                                            throws Exception {
        if (jsonObjects == null || jsonObjects == "") return ResponseEntity.badRequest().build();

        List<HumanBeingDto> humansDto = importService.importHumanBeings(jsonObjects);

        for (HumanBeingDto humanDto : humansDto) {

            Coordinates coords = null;
            Car car = null;
            if (humanDto.getCarName() != null && humanDto.getCarCool() != null) {
                CoordinatesDto coordDto = new CoordinatesDto();
                coordDto.setX(humanDto.getCoordinatesX());
                coordDto.setY(humanDto.getCoordinatesY());
                coordDto.setAdminsCanEdit(true);
                coords = new Coordinates(coordDto, userDetails.getIsUser());
                coords = coordinatesService.createCoordinates(coords);
                if (coords == null) throw new Exception();
                humanDto.setCoordinates(coords.getId());
            }
            if (humanDto.getCoordinatesX() != null) {
                CarDto carDto = new CarDto();
                carDto.setName(humanDto.getCarName());
                carDto.setCool(humanDto.getCarCool());
                carDto.setAdminsCanEdit(true);
                car = new Car(carDto, userDetails.getIsUser());
                car = carService.createCar(car);
                if (car == null) throw new Exception();
                humanDto.setCar(car.getId());
            } else {
                throw new Exception();
            }

            HumanBeing human = new HumanBeing(humanDto, userDetails.getIsUser(), coords);
            if (car != null) {
                human.setCar(car);
            }
            humanBeingService.createHumanBeing(human);
        }
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PostMapping("coordinates/import")
    public ResponseEntity<?> jsonImportCoordinates(@RequestParam String jsonObjects, @AuthenticationPrincipal IsUserDetails userDetails)
                                                                            throws Exception {
        if (jsonObjects == null || jsonObjects == "") return ResponseEntity.badRequest().build();

        List<CoordinatesDto> coordinatesDto = importService.importCoordinates(jsonObjects);

        for (HumanBeingDto humanDto : humansDto) {
            System.out.println(humanDto.getAdminsCanEdit());
            Coordinates coords = null;
            Car car = null;
            if (humanDto.getCarName() != null && humanDto.getCarCool() != null) {
                CoordinatesDto coordDto = new CoordinatesDto();
                coordDto.setX(humanDto.getCoordinatesX());
                coordDto.setY(humanDto.getCoordinatesY());
                coordDto.setAdminsCanEdit(true);
                coords = new Coordinates(coordDto, userDetails.getIsUser());
                coords = coordinatesService.createCoordinates(coords);
                if (coords == null) throw new Exception();
                humanDto.setCoordinates(coords.getId());
            }
            if (humanDto.getCoordinatesX() != null) {
                CarDto carDto = new CarDto();
                carDto.setName(humanDto.getCarName());
                carDto.setCool(humanDto.getCarCool());
                carDto.setAdminsCanEdit(true);
                car = new Car(carDto, userDetails.getIsUser());
                car = carService.createCar(car);
                if (car == null) throw new Exception();
                humanDto.setCar(car.getId());
            } else {
                throw new Exception();
            }

            HumanBeing human = new HumanBeing(humanDto, userDetails.getIsUser(), coords);
            if (car != null) {
                human.setCar(car);
            }
            humanBeingService.createHumanBeing(human);
        }
        return ResponseEntity.ok().build();
    }
    
}
