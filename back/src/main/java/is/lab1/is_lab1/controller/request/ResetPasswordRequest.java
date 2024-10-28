package is.lab1.is_lab1.controller.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    
    String token;

    String newPassword;
    
}
