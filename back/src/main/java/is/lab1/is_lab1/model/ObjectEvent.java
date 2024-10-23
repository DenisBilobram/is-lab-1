package is.lab1.is_lab1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "object_events")
public class ObjectEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;


    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    private IsUser isUser;

    public ObjectEvent(EventType eventType, LocalDateTime timestamp, IsUser user) {

        this.eventType = eventType;
        this.timestamp = timestamp;
        this.isUser = user;

    }

}

