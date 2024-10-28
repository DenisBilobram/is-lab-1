package is.lab1.is_lab1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import is.lab1.is_lab1.controller.exception.RootsRequestAlreadyExistException;
import is.lab1.is_lab1.model.IsUser;
import is.lab1.is_lab1.model.RootsRequest;
import is.lab1.is_lab1.repository.IsUserRepository;
import is.lab1.is_lab1.repository.RootsRequestRepositry;
import jakarta.transaction.Transactional;

@Service
public class RootsRequestService {
    
    @Autowired
    RootsRequestRepositry rootsRequestRepositry;

    @Autowired
    IsUserRepository isUserRepository;

    public List<RootsRequest> getAllActive() {

        return rootsRequestRepositry.findAllByIsActive(true);
        
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public RootsRequest get(Long id) {

        return this.rootsRequestRepositry.getReferenceById(id);

    }

    public RootsRequest create(RootsRequest rootsRequest) throws RootsRequestAlreadyExistException {

        if (rootsRequestRepositry.findAll().isEmpty()) {
            aprove(rootsRequestRepositry.save(rootsRequest));
            return null;
        }
        
        if (rootsRequestRepositry.findByIsUser(rootsRequest.getIsUser()) != null || rootsRequest.getIsUser().getRoles().contains(rootsRequest.getRole()))
            throw new RootsRequestAlreadyExistException("Roots request already exits.");

        RootsRequest request = rootsRequestRepositry.save(rootsRequest);

        return request;

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public void aprove(RootsRequest rootsRequest) {

        IsUser user = rootsRequest.getIsUser();
        user.addRole(rootsRequest.getRole());
        isUserRepository.save(user);

        rootsRequest.setIsActive(false);
        rootsRequestRepositry.save(rootsRequest);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void decline(RootsRequest rootsRequest) {

        rootsRequest.setIsActive(false);
        rootsRequestRepositry.save(rootsRequest);

    }





}
