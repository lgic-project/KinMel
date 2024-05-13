package com.lagrandee.kinMel.controllers;

import com.lagrandee.kinMel.KinMelCustomMessage;
import com.lagrandee.kinMel.bean.request.ProductRequest;
import com.lagrandee.kinMel.bean.response.ProductResponse;
import com.lagrandee.kinMel.bean.response.ProductResponseWithStatus;
import com.lagrandee.kinMel.service.implementation.CategoryServiceImplementation;
import com.lagrandee.kinMel.service.implementation.ProductServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/kinMel")
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceImplementation productServiceImplementation;

    @PreAuthorize("")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/products")
    public ResponseEntity<?> addProduct(
            @RequestPart("productDetails") ProductRequest productRequest,
            @RequestPart("productImages") MultipartFile[] productImages
    ) {
        String newProduct = productServiceImplementation.createNewProduct(productRequest, productImages);
        KinMelCustomMessage customMessage=new KinMelCustomMessage(HttpStatus.CREATED.value(),newProduct ,System.currentTimeMillis());
        return new ResponseEntity<>(customMessage, HttpStatus.OK);
    }

    @PreAuthorize("")
    @GetMapping("/products")
    public ResponseEntity<?> getAllProduct( @RequestParam(required = false) String productName,
                                            @RequestParam(required = false) String brandName,
                                            @RequestParam(required = false) String sortBy,
                                            @RequestParam(required = false) String categoryName,
                                            @RequestParam(required = false) Long maxPrice){
        List<ProductResponse> allProduct = productServiceImplementation.getAllProduct(productName, sortBy, categoryName, maxPrice, brandName);
        ProductResponseWithStatus response = new ProductResponseWithStatus();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.name());
        if (allProduct.isEmpty()){
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
     }
        response.setStatusValue(HttpStatus.OK.name());
        response.setProducts(allProduct);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
