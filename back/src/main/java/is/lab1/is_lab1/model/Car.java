package is.lab1.is_lab1.model;

import java.util.ArrayList;
import java.util.List;

import is.lab1.is_lab1.controller.request.CarDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Car {

    public Car(CarDto car, IsUser user) {
        this.name = car.getName();
        this.cool = car.getCool();
        this.isUser = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name; //Поле может быть null

    @Column
    private Boolean cool; //Поле может быть null

    @ManyToOne
    private IsUser isUser;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private List<ObjectEvent> objectEvents = new ArrayList<>();

    public void setDtoData(CarDto dto) {
        this.name = dto.getName();
        this.cool = dto.getCool();
    }

}