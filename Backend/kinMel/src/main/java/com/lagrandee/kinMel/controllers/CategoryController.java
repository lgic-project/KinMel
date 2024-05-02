package com.lagrandee.kinMel.controllers;

import com.lagrandee.kinMel.bean.UserDetail;
import com.lagrandee.kinMel.bean.request.CategoryRequest;
import com.lagrandee.kinMel.bean.response.CategoryResponse;
import com.lagrandee.kinMel.service.implementation.CategoryServiceImplementation;
import com.lagrandee.kinMel.service.implementation.UserServiceImplementation;
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
        return categoryServiceImplementation.insertNewCategory(categoryRequest.getCategoryName(), categoryRequest.getCategoryDescription());
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable int categoryId,@RequestBody CategoryRequest categoryRequest) {
        return categoryServiceImplementation.updateCategory(categoryId,categoryRequest);
    }
    @PreAuthorize("")
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategory(){
        List<CategoryResponse> categories = categoryServiceImplementation.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

}
