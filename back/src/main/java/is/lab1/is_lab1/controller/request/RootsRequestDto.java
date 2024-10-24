package is.lab1.is_lab1.controller.request;

import java.time.LocalDateTime;

import javax.management.relation.Role;

import lombok.Data;

@Data
public class RootsRequestDto {

    private LocalDateTime creationDate;

    private Role role;

    String isUser;


}
