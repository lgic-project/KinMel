package com.lagrandee.kinMel.controllers;

import com.lagrandee.kinMel.bean.request.CategoryRequest;
import com.lagrandee.kinMel.bean.response.CategoryResponse;
import com.lagrandee.kinMel.bean.response.ResponseWithStatus;
import com.lagrandee.kinMel.bean.response.SingleDataResponse;
import com.lagrandee.kinMel.bean.response.SingleResponseWithStatus;
import com.lagrandee.kinMel.service.implementation.CategoryServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kinMel")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImplementation categoryServiceImplementation;


    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest) {
        return categoryServiceImplementation.insertNewCategory(categoryRequest.getCategoryName(), categoryRequest.getCategoryDescription(),categoryRequest.getCategoryImage(),categoryRequest.getImageFormat());
    }


    @PreAuthorize("hasRole('Seller')")
    @PostMapping("/categories/request")
    public ResponseEntity<?> requestCategory(@RequestBody CategoryRequest categoryRequest){
        String result = categoryServiceImplementation.requestNewCategory(categoryRequest.getCategoryName(), categoryRequest.getCategoryDescription(), categoryRequest.getCategoryImage(), categoryRequest.getImageFormat());
        SingleDataResponse<String> response = new SingleDataResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.name());
        response.setData(result);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable int categoryId,@RequestBody CategoryRequest categoryRequest) {
        return categoryServiceImplementation.updateCategory(categoryId,categoryRequest);
    }
    @PreAuthorize("")
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategory(){
        List<CategoryResponse> categories = categoryServiceImplementation.getAllCategories();
        if (categories.isEmpty()){
            SingleResponseWithStatus r = new SingleResponseWithStatus();
            r.setStatus(HttpStatus.OK.value());
            r.setStatusValue(HttpStatus.OK.name());
            r.setData("No data found");
            return new ResponseEntity<>(r, HttpStatus.OK);
        }
        ResponseWithStatus response = new ResponseWithStatus();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.name());
        response.setStatusValue(HttpStatus.OK.name());
        response.setData(categories);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
