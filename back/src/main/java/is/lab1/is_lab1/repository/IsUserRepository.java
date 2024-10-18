package is.lab1.is_lab1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import is.lab1.is_lab1.model.IsUser;

@Repository
public interface IsUserRepository extends JpaRepository<IsUser, Long> {

    Optional<IsUser> findByUsername(String username);
    
    Optional<IsUser> findByPassword(String password);

    Optional<IsUser> findByEmail(String password);
}