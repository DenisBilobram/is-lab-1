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
    }
    
    Long id;

    @NotNull
    @Min(value = -418, message = "Значение должно быть больше -419")
    private Double x;

    @NotNull
    @Min(value = -452, message = "Значение должно быть больше -452")
    private float y;

    private String isUser;

    private List<ObjectEventDto> objectEvents;

    ObjectOperationType type;

}
