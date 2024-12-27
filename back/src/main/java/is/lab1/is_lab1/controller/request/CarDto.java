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
        this.adminsCanEdit = car.getAdminsCanEdit();
    }

    private Long id;

    @NotNull(message = "Поле 'name' не может быть пустым.")
    private String name;

    @NotNull(message = "Поле 'cool' обязательно для заполнения.")
    private Boolean cool;

    private String isUser;

    @NotNull(message = "Поле 'adminsCanEdit' обязательно для заполнения.")
    private Boolean adminsCanEdit;

    private List<ObjectEventDto> objectEvents;

    private ObjectOperationType type;
}