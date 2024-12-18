package is.lab1.is_lab1.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import is.lab1.is_lab1.controller.request.CarDto;
import is.lab1.is_lab1.controller.request.CoordinatesDto;
import is.lab1.is_lab1.controller.request.HumanBeingDto;
import is.lab1.is_lab1.model.Coordinates;
import is.lab1.is_lab1.model.HumanBeing;
import is.lab1.is_lab1.repository.CarRepository;
import is.lab1.is_lab1.repository.CoordinatesRepository;
import is.lab1.is_lab1.repository.HumanBeingRepository;

@Service
public class ObjectsImportService {

    private ObjectMapper objectMapper;

    public ObjectsImportService() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<HumanBeingDto> importHumanBeings(String jsonObjects) throws JsonMappingException, JsonProcessingException {
        List<HumanBeingDto> dtos = objectMapper.readValue(jsonObjects, objectMapper.getTypeFactory().constructCollectionType(List.class, HumanBeingDto.class));
        return dtos;
    }

    public List<CoordinatesDto> importCoordinates(String jsonObjects) throws JsonMappingException, JsonProcessingException {
        List<CoordinatesDto> dtos = objectMapper.readValue(jsonObjects, objectMapper.getTypeFactory().constructCollectionType(List.class, CoordinatesDto.class));
        return dtos;
    }

    public List<CarDto> importCars(String jsonObjects) throws JsonMappingException, JsonProcessingException {
        List<CarDto> dtos = objectMapper.readValue(jsonObjects, objectMapper.getTypeFactory().constructCollectionType(List.class, CarDto.class));
        return dtos;
    }

}
