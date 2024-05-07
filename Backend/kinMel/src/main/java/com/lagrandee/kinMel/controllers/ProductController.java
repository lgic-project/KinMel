package com.lagrandee.kinMel.controllers;

import com.lagrandee.kinMel.KinMelCustomMessage;
import com.lagrandee.kinMel.bean.request.ProductRequest;
import com.lagrandee.kinMel.service.implementation.CategoryServiceImplementation;
import com.lagrandee.kinMel.service.implementation.ProductServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
