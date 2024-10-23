package is.lab1.is_lab1.controller.request;

import java.util.List;

import is.lab1.is_lab1.model.Car;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarDto {

    public CarDto(Car car) {
        this.id = car.getId();
        this.name = car.getName();
        this.cool = car.getCool();
        this.isUser = car.getIsUser().getUsername();
        this.objectEvents = car.getObjectEvents().stream().map(event -> new ObjectEventDto(event)).toList();
    }
    
    Long id;

    @NotNull
    private String name;

    @NotNull
    private Boolean cool;

    private String isUser;

    private List<ObjectEventDto> objectEvents;

    ObjectOperationType type;

}
