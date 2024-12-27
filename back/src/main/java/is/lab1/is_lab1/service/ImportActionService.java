package is.lab1.is_lab1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import is.lab1.is_lab1.controller.request.CarDto;
import is.lab1.is_lab1.controller.request.CoordinatesDto;
import is.lab1.is_lab1.controller.request.HumanBeingDto;
import is.lab1.is_lab1.model.Car;
import is.lab1.is_lab1.model.Coordinates;
import is.lab1.is_lab1.model.HumanBeing;
import is.lab1.is_lab1.model.ImportAction;
import is.lab1.is_lab1.model.IsUser;
import is.lab1.is_lab1.model.ObjectType;
import is.lab1.is_lab1.repository.ImportActionRepository;
import jakarta.validation.ValidationException;

@Service
public class ImportActionService {
    
    @Autowired
    private ImportActionRepository importActionRepository;

    @Autowired
    ValidatorService validatorService;

    private ObjectMapper objectMapper;

    public ImportActionService() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<HumanBeingDto> importHumanBeings(String jsonObjects) throws JsonMappingException, JsonProcessingException, ValidationException {
        List<HumanBeingDto> dtos = objectMapper.readValue(jsonObjects, objectMapper.getTypeFactory().constructCollectionType(List.class, HumanBeingDto.class));
        for (HumanBeingDto dto : dtos) {
            dto.setCoordinates(Long.valueOf(-1)); // Для обхода @NotNull на coordinates поле.
        }
        validatorService.validateObjects(dtos);
        return dtos;
    }

    public List<CoordinatesDto> importCoordinates(String jsonObjects) throws JsonMappingException, JsonProcessingException, ValidationException {
        List<CoordinatesDto> dtos = objectMapper.readValue(jsonObjects, objectMapper.getTypeFactory().constructCollectionType(List.class, CoordinatesDto.class));
        validatorService.validateObjects(dtos);
        return dtos;
    }

    public List<CarDto> importCars(String jsonObjects) throws JsonMappingException, JsonProcessingException, ValidationException {
        List<CarDto> dtos = objectMapper.readValue(jsonObjects, objectMapper.getTypeFactory().constructCollectionType(List.class, CarDto.class));
        validatorService.validateObjects(dtos);
        return dtos;
    }

    public <T> boolean createImporAction(List<T> objects, Class<T> clazz, IsUser user) {

        ImportAction action = new ImportAction(objects, ObjectType.fromClass(clazz), user);

        importActionRepository.save(action);

        return true;

    }

    public List<ImportAction> getHumanBeingHistory(IsUser user) {
        return getImportHistory(user, HumanBeing.class);
    }

    public List<ImportAction> getCarHistoty(IsUser user) {
        return getImportHistory(user, Car.class);
    }

    public List<ImportAction> getCoordinatesHistoty(IsUser user) {
        return getImportHistory(user, Coordinates.class);
    }

    public <T> List<ImportAction> getImportHistory(IsUser user, Class<T> clazz) {
        if (user.isAdmin()) return importActionRepository.findAllByImportActionType(ObjectType.fromClass(clazz));
        return importActionRepository.findAllByIsUserAndImportActionType(user, ObjectType.fromClass(clazz));
    }

}
