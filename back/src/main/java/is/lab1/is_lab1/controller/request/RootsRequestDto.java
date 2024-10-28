package is.lab1.is_lab1.controller.request;

import java.time.format.DateTimeFormatter;

import is.lab1.is_lab1.model.Role;
import is.lab1.is_lab1.model.RootsRequest;
import lombok.Data;

@Data
public class RootsRequestDto {

    public RootsRequestDto(RootsRequest request) {
        this.id = request.getId();
        this.creationDate = request.getCreationDate().format(DateTimeFormatter.ofPattern("d MMM uuuu"));
        this.role = request.getRole();
        this.isUser = request.getIsUser().getUsername();
    }

    private Long id;

    private String creationDate;

    private Role role;

    String isUser;


}
