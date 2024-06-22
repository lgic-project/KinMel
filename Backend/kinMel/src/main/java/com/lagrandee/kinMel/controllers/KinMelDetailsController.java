package com.lagrandee.kinMel.controllers;

import com.lagrandee.kinMel.bean.response.DashboardCategoryResponse;
import com.lagrandee.kinMel.bean.response.EntityDetail;
import com.lagrandee.kinMel.bean.response.SingleDataResponse;
import com.lagrandee.kinMel.service.implementation.CategoryServiceImplementation;
import com.lagrandee.kinMel.service.implementation.DetailsServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kinMel")
@RequiredArgsConstructor
public class KinMelDetailsController {
    private final DetailsServiceImplementation detailServiceImplementation;

    @GetMapping("/entity")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> getKinmelDetails(){
        EntityDetail entityDetail = detailServiceImplementation.fetchEntityDetails();
        SingleDataResponse<EntityDetail> response = new SingleDataResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.name());
        response.setData(entityDetail);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/category/details")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> getCategoryDetails(){
        List<DashboardCategoryResponse> dashboardCategoryResponses = detailServiceImplementation.fetchEachCategoryDetails();
        SingleDataResponse<List<DashboardCategoryResponse>> response = new SingleDataResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.name());
        response.setData(dashboardCategoryResponses);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
