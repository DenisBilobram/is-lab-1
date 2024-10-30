package is.lab1.is_lab1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/objects", "/profile/**", "/operations/**", "/vizualization/**", "/auth/**"})
public class AngularController {
    
    @GetMapping()
    public String toMain() {
        return "forward:/";
    }
    
}
