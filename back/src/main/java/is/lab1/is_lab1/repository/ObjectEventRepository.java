package is.lab1.is_lab1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import is.lab1.is_lab1.model.ObjectEvent;

public interface ObjectEventRepository extends JpaRepository<ObjectEvent, Long> {
    
}
