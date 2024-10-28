package is.lab1.is_lab1.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {

  private final String jwt;
  private final String username;
  private final String email;

}
