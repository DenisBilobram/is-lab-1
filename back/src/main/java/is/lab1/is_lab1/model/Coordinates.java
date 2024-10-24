package is.lab1.is_lab1.model;


import java.util.ArrayList;
import java.util.List;

import is.lab1.is_lab1.controller.request.CoordinatesDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coordinates {

    public Coordinates(CoordinatesDto dto, IsUser user) {
        this.x = dto.getX();
        this.y = dto.getY();
        this.isUser = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = -418, message = "Значение должно быть больше -419")
    @Column(nullable = false)
    private Double x; //Значение поля должно быть больше -419, Поле не может быть null

    @Min(value = -452, message = "Значение должно быть больше -452")
    @Column(nullable = false)
    private float y; //Значение поля должно быть больше -453

    @ManyToOne
    private IsUser isUser;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id")
    private List<ObjectEvent> objectEvents = new ArrayList<>();

    public void setDtoData(CoordinatesDto dto) {
        this.x = dto.getX();
        this.y = dto.getY();
    }
    
}
