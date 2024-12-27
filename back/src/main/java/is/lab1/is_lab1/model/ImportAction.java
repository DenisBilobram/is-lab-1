package is.lab1.is_lab1.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ImportAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ObjectType importActionType;

    @OneToMany
    @JoinColumn(name = "import_action_id") 
    private List<HumanBeing> humanBeings;

    @OneToMany
    @JoinColumn(name = "import_action_id")
    private List<Car> cars;

    @OneToMany
    @JoinColumn(name = "import_action_id")
    private List<Coordinates> coordinates;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @ManyToOne
    private IsUser isUser;

    @Column
    private Integer count;

    public <T> ImportAction(List<T> objects, ObjectType type, IsUser user) {
        this.importActionType = type;
        this.isUser = user;
        this.count = objects.size();
        if (type == ObjectType.HUMAN_BEING) {
            this.humanBeings = objects.stream().map(el -> (HumanBeing)el).toList();
        } else if (type == ObjectType.CAR) {
            this.cars = objects.stream().map(el -> (Car)el).toList();
        } else if (type == ObjectType.COORDINATES) {
            this.coordinates = objects.stream().map(el -> (Coordinates)el).toList();
        }
    }

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }

}

