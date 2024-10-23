package is.lab1.is_lab1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import is.lab1.is_lab1.service.IsUserDetails;
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

        ResponseCookie jwtCookie = ResponseCookie.from("JWT-TOKEN", jwt)
            .path("/")
            .secure(false)
            .maxAge(7 * 24 * 60 * 60)
            .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(new AuthenticationResponse(jwt, newUser.getUsername(), newUser.getEmail(), newUser.isAdmin()));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        
        final IsUser isUser = userRepository.findByUsername(authenticationRequest.getUsername()).get();
        
        final String jwt = jwtTokenUtil.generateToken(isUser.getUsername());

        ResponseCookie jwtCookie = ResponseCookie.from("JWT-TOKEN", jwt)
            .path("/")
            .secure(false)
            .maxAge(7 * 24 * 60 * 60)
            .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(new AuthenticationResponse(jwt, isUser.getUsername(), isUser.getEmail(), isUser.isAdmin()));
    }

    // @PostMapping("/roots")
    // public ResponseEntity<?> createRootsRequest(@AuthenticationPrincipal IsUserDetails userDetails) {
        


    // }
    
}
