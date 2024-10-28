package is.lab1.is_lab1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import is.lab1.is_lab1.controller.exception.EmailNotFoundException;
import is.lab1.is_lab1.controller.exception.RegistrationFailException;
import is.lab1.is_lab1.controller.request.AuthenticationRequest;
import is.lab1.is_lab1.model.IsUser;
import is.lab1.is_lab1.model.Role;
import is.lab1.is_lab1.repository.IsUserRepository;

@Service
public class IsUserDetailsService implements UserDetailsService {

    @Autowired
    private IsUserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        IsUser user = getByUsername(username);

        return new IsUserDetails(user, user.getGrantedAuthorities());
    }

    public IsUser getByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));
    }

    public IsUser getByEmail(String email) throws EmailNotFoundException {

        return userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException("No user found with email: " + email));

    }

    public IsUser registerUser(AuthenticationRequest registrationRequest) throws RegistrationFailException {

        if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            throw new RegistrationFailException("User with this username already exists.");
        }

        if (userRepository.findByPassword(passwordEncoder.encode(registrationRequest.getPassword())).isPresent()) {
            throw new RegistrationFailException("User with this password already exists.");
        }

        if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            throw new RegistrationFailException("User with this email already exists.");
        }

        IsUser newUser = new IsUser();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        newUser.setEmail(registrationRequest.getEmail());
        newUser.addRole(Role.USER);
        userRepository.save(newUser);

        return newUser;
    }
    
}
