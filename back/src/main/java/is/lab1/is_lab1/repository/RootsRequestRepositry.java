package is.lab1.is_lab1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import is.lab1.is_lab1.model.IsUser;
import is.lab1.is_lab1.model.RootsRequest;

@Repository
public interface RootsRequestRepositry extends JpaRepository<RootsRequest, Long> {

    public RootsRequest findByIsUser(IsUser user);
    
}
