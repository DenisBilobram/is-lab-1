package is.lab1.is_lab1.controller.request;

import java.util.List;

import is.lab1.is_lab1.model.Coordinates;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoordinatesDto {

    public CoordinatesDto(Coordinates coords) {
        this.id = coords.getId();
        this.x = coords.getX();
        this.y = coords.getY();
        this.isUser = coords.getIsUser().getUsername();
        this.objectEvents = coords.getObjectEvents().stream().map(event -> new ObjectEventDto(event)).toList();
        this.adminsCanEdit = coords.getAdminsCanEdit();
    }

    private Long id;

    @NotNull(message = "Field coordinates 'x' cannot be empty.")
    @Min(value = -418, message = "Значение поля 'x' должно быть больше -419.")
    private Double x; // Значение поля должно быть больше -419, Field не может быть null

    @NotNull(message = "Field coordinates 'y' cannot be empty.")
    @Min(value = -452, message = "Значение поля 'y' должно быть больше -452.")
    private float y; // Значение поля должно быть больше -452

    private String isUser;

    @NotNull(message = "Field 'adminsCanEdit' must be entered.")
    private Boolean adminsCanEdit;

    private List<ObjectEventDto> objectEvents;

    private ObjectOperationType type;
}