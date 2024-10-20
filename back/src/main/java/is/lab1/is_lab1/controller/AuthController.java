package is.lab1.is_lab1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import is.lab1.is_lab1.service.IsUserDetailsService;
import is.lab1.is_lab1.component.JwtUtil;
import is.lab1.is_lab1.controller.exception.RegistrationFailException;
import is.lab1.is_lab1.controller.request.AuthenticationRequest;
import is.lab1.is_lab1.controller.request.AuthenticationResponse;
import is.lab1.is_lab1.model.IsUser;
import is.lab1.is_lab1.repository.IsUserRepository;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IsUserDetailsService userDetailsService;

    @Autowired
    private IsUserRepository userRepository;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping("status")
    public ResponseEntity<?> status() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody AuthenticationRequest registrationRequest) throws RegistrationFailException {

        IsUser newUser = userDetailsService.registerUser(registrationRequest);

        final String jwt = jwtTokenUtil.generateToken(newUser.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(jwt, newUser.getUsername(), newUser.getEmail()));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        
        final IsUser isUser = userRepository.findByUsername(authenticationRequest.getUsername()).get();
        
        final String jwt = jwtTokenUtil.generateToken(isUser.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(jwt, isUser.getUsername(), isUser.getEmail()));
    }

}
