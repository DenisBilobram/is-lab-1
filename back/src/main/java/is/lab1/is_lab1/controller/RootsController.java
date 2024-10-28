package is.lab1.is_lab1.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import is.lab1.is_lab1.controller.exception.RootsRequestAlreadyExistException;
import is.lab1.is_lab1.controller.request.RootsRequestDto;
import is.lab1.is_lab1.model.Role;
import is.lab1.is_lab1.model.RootsRequest;
import is.lab1.is_lab1.service.IsUserDetails;
import is.lab1.is_lab1.service.RootsRequestService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("api/roots")
public class RootsController { 

    @Autowired
    RootsRequestService rootsRequestService;

    @GetMapping
    public ResponseEntity<Set<Role>> getRoles(@AuthenticationPrincipal IsUserDetails userDetails) {
        return new ResponseEntity<>(userDetails.getIsUser().getRoles(), HttpStatus.OK);
    }
    
    @PostMapping("/request")
    public ResponseEntity<?> createRootsRequest(@AuthenticationPrincipal IsUserDetails userDetails) throws RootsRequestAlreadyExistException {
        
        RootsRequest rootsRequest = new RootsRequest(userDetails.getIsUser(), Role.ADMIN);

        rootsRequestService.create(rootsRequest);

        return ResponseEntity.ok(HttpStatus.CREATED);

    }

    @GetMapping("/request")
    public ResponseEntity<List<RootsRequestDto>> getRootsRequestsList() {
        return new ResponseEntity<>(rootsRequestService.getAllActive().stream().map(RootsRequestDto::new).toList(), HttpStatus.OK);
    }
    
    @PutMapping("request/aprove/{id}")
    public ResponseEntity<?> aproveRequest(@PathVariable Long id) {
        
        RootsRequest request = rootsRequestService.get(id);

        rootsRequestService.aprove(request);

        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PutMapping("request/decline/{id}")
    public ResponseEntity<?> declineRequest(@PathVariable Long id) {
        
        RootsRequest request = rootsRequestService.get(id);

        rootsRequestService.decline(request);

        return ResponseEntity.ok(HttpStatus.OK);

    }
    

}
