package is.lab1.is_lab1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import is.lab1.is_lab1.controller.exception.RootsRequestAlreadyExist;
import is.lab1.is_lab1.model.RootsRequest;
import is.lab1.is_lab1.repository.RootsRequestRepositry;
import jakarta.transaction.Transactional;

@Service
public class RootsRequestService {
    
    @Autowired
    RootsRequestRepositry rootsRequestRepositry;

    public List<RootsRequest> getAll() {

        return rootsRequestRepositry.findAll();
        
    }

    public RootsRequest create(RootsRequest rootsRequest) throws RootsRequestAlreadyExist {

        if (!(rootsRequestRepositry.findByIsUser(rootsRequest.getIsUser()) == null)) throw new RootsRequestAlreadyExist("Roots request already exits.");

        RootsRequest request = rootsRequestRepositry.save(rootsRequest);

        if (rootsRequestRepositry.findAll().isEmpty()) aprove(request);

        return request;

    }

    @Transactional
    public void aprove(RootsRequest rootsRequest) {

        rootsRequest.getIsUser().addRole(rootsRequest.getRole());
        rootsRequestRepositry.delete(rootsRequest);

    }

    public void decline(RootsRequest rootsRequest) {

        rootsRequestRepositry.delete(rootsRequest);

    }





}
