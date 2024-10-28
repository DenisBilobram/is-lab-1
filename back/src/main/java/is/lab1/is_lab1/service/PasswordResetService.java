package is.lab1.is_lab1.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import is.lab1.is_lab1.config.Sha256PasswordEncoder;
import is.lab1.is_lab1.controller.exception.InvalidResetTokenException;
import is.lab1.is_lab1.model.IsUser;
import is.lab1.is_lab1.model.PasswordResetToken;
import is.lab1.is_lab1.repository.IsUserRepository;
import is.lab1.is_lab1.repository.PasswordResetTokenRepository;
import jakarta.transaction.Transactional;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private IsUserRepository userRepository;

    @Autowired
    private Sha256PasswordEncoder passwordEncoder;

    public PasswordResetToken createPasswordResetTokenForUser(IsUser user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        return tokenRepository.save(myToken);
    }

    public PasswordResetToken validatePasswordResetToken(String token) throws InvalidResetTokenException {
        PasswordResetToken passToken = tokenRepository.findByToken(token);
        if (passToken == null) throw new InvalidResetTokenException();
        if (passToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(passToken);
            throw new InvalidResetTokenException();
        }
        return passToken;
    }

    @Transactional
    public void resetPassword(IsUser user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
