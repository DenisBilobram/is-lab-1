package is.lab1.is_lab1.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import is.lab1.is_lab1.controller.request.HumanBeingDto;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"coordinates", "car", "isUser"})
public class HumanBeing implements Ownable {

    public HumanBeing(HumanBeingDto dto, IsUser user, Coordinates coords) {
        this.name = dto.getName();
        this.coordinates = coords;
        this.realHero = dto.isRealHero();
        this.hasToothpick = dto.isHasToothpick();
        this.mood = dto.getMood();
        this.impactSpeed = dto.getImpactSpeed();
        this.soundtrackName = dto.getSoundtrackName();
        this.weaponType = dto.getWeaponType();
        this.isUser = user;
        this.adminsCanEdit = dto.getAdminsCanEdit();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @NotBlank
    @Column(nullable = false)
    private String name; //Поле не может быть null, Строка не может быть пустой

    @ManyToOne
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id", nullable = false)
    private Coordinates coordinates; //Поле не может быть null

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Column
    private boolean realHero;

    @Column
    private boolean hasToothpick;

    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id", nullable = true)
    private Car car; //Поле может быть null

    @Column(name = "mood", nullable = false)
    private Mood mood; //Поле не может быть null

    @Column
    private int impactSpeed;

    @Column
    private String soundtrackName; //Поле не может быть null

    @Column(name = "weapon_type", nullable = false)
    private WeaponType weaponType; //Поле не может быть null

    @Column(nullable = false)
    private Boolean adminsCanEdit;

    @ManyToOne
    private IsUser isUser;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "human_being_id", referencedColumnName = "id")
    private List<ObjectEvent> objectEvents = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }

    public void setDtoData(HumanBeingDto dto) {
        this.name = dto.getName();
        this.realHero = dto.isRealHero();
        this.hasToothpick = dto.isHasToothpick();
        this.mood = dto.getMood();
        this.impactSpeed = dto.getImpactSpeed();
        this.soundtrackName = dto.getSoundtrackName();
        this.weaponType = dto.getWeaponType();
    }

    @Override
    public Boolean isAdminsCanEdit() {
        return this.adminsCanEdit;
    }

}