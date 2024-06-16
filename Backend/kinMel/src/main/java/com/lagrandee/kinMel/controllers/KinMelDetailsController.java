package com.lagrandee.kinMel.controllers;

import com.lagrandee.kinMel.service.implementation.CategoryServiceImplementation;
import com.lagrandee.kinMel.service.implementation.DetailsServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kinMel")
@RequiredArgsConstructor
public class KinMelDetailsController {
    private final DetailsServiceImplementation detailServiceImplementation;

    @GetMapping("/entity")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> getKinmelDetails(){
        detailServiceImplementation.fetchEntityDetails();
        return null;
    }


}
