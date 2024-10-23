package is.lab1.is_lab1.controller.request;

import java.time.format.DateTimeFormatter;
import java.util.List;

import is.lab1.is_lab1.model.HumanBeing;
import is.lab1.is_lab1.model.Mood;
import is.lab1.is_lab1.model.WeaponType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class HumanBeingDto {

    public HumanBeingDto(HumanBeing human) {

        this.id = human.getId();
        this.name = human.getName();
        this.coordinates = human.getCoordinates().getId();
        this.creationDate = human.getCreationDate().format(DateTimeFormatter.ofPattern("d MMM uuuu"));
        this.realHero = human.isRealHero();
        this.hasToothpick = human.isHasToothpick();
        this.car = human.getCar() == null ? null : human.getCar().getId();
        this.mood = human.getMood();
        this.impactSpeed = human.getImpactSpeed();
        this.soundtrackName = human.getSoundtrackName();
        this.weaponType = human.getWeaponType();
        this.isUser = human.getIsUser().getUsername();
        this.objectEvents = human.getObjectEvents().stream().map(event -> new ObjectEventDto(event)).toList();
    }

    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @NotNull
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull
    private Long coordinates; //Поле не может быть null

    private String creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @NotNull
    private boolean realHero;

    @NotNull
    private boolean hasToothpick;

    private Long car; //Поле может быть null

    @NotNull
    private Mood mood; //Поле не может быть null

    @NotNull
    private int impactSpeed;

    @NotNull
    private String soundtrackName; //Поле не может быть null
    
    @NotNull
    private WeaponType weaponType; //Поле не может быть null

    private String isUser;

    private List<ObjectEventDto> objectEvents;

    ObjectOperationType type;
}
