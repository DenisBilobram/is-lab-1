package is.lab1.is_lab1.controller.request;

import java.time.format.DateTimeFormatter;

import is.lab1.is_lab1.model.EventType;
import is.lab1.is_lab1.model.ObjectEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ObjectEventDto {
    
    private EventType eventType;

    private String timestamp;

    private String isUser;

    public ObjectEventDto(ObjectEvent event) {

        this.eventType = event.getEventType();
        this.timestamp = event.getTimestamp().format(DateTimeFormatter.ofPattern("d MMM uuuu"));
        this.isUser = event.getIsUser().getUsername();

    }
}
