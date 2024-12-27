package is.lab1.is_lab1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import is.lab1.is_lab1.model.ImportAction;
import is.lab1.is_lab1.model.IsUser;
import is.lab1.is_lab1.model.ObjectType;

@Repository
public interface ImportActionRepository extends JpaRepository<ImportAction, Long> {

    public List<ImportAction> findAllByIsUserAndImportActionType(IsUser user, ObjectType type);

    public List<ImportAction> findAllByImportActionType(ObjectType type);

}