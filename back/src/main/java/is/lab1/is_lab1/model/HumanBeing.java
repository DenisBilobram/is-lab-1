package is.lab1.is_lab1.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

@Data
@Entity
public class HumanBeing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @NotBlank
    @Column(nullable = false)
    private String name; //Поле не может быть null, Строка не может быть пустой

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id", nullable = false)
    private Coordinates coordinates; //Поле не может быть null

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Column
    private boolean realHero;

    @Column
    private boolean hasToothpick;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car; //Поле может быть null

    @ElementCollection(targetClass = Mood.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "mood", nullable = false)
    private Mood mood; //Поле не может быть null

    @Column
    private int impactSpeed;

    @Column
    private String soundtrackName; //Поле не может быть null

    @ElementCollection(targetClass = WeaponType.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "weapon-type")
    private WeaponType weaponType; //Поле не может быть null
}