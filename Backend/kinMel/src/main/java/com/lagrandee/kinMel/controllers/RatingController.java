package com.lagrandee.kinMel.controllers;

import com.lagrandee.kinMel.bean.request.RatingRequest;
import com.lagrandee.kinMel.bean.response.SingleDataResponse;
import com.lagrandee.kinMel.service.implementation.RatingServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kinMel/ratings")
public class RatingController {

    private final RatingServiceImplementation ratingServiceImplementation;

    @PostMapping
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<?> addRating(@RequestBody RatingRequest request) {
        String result = ratingServiceImplementation.addRating(request);
        SingleDataResponse<String> response = new SingleDataResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.name());
        response.setData(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
