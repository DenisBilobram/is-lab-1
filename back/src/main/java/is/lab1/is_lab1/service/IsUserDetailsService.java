package is.lab1.is_lab1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        IsUser user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("This is no user with this username."));
     
        return new User(user.getUsername(), user.getPassword(), user.getGrantedAuthorities());
    }

    public IsUser registerUser(AuthenticationRequest registrationRequest) throws RegistrationFailException {

        if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            throw new RegistrationFailException("User with this username already exists.");
        }

        if (userRepository.findByPassword(passwordEncoder.encode(registrationRequest.getPassword())).isPresent()) {
            throw new RegistrationFailException("User with this password already exists.");
        }
        if (userRepository.findByPassword(registrationRequest.getEmail()).isPresent()) {
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
