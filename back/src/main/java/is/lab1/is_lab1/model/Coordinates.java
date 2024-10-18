package is.lab1.is_lab1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Entity
public class Coordinates {

    @Min(value = -418, message = "Значение должно быть больше -419")
    @Column(nullable = false)
    private Double x; //Значение поля должно быть больше -419, Поле не может быть null

    @Min(value = -418, message = "Значение должно быть больше -419")
    @Column
    private float y; //Значение поля должно быть больше -453
    
}
