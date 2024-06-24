package com.lagrandee.kinMel.controllers;

import com.lagrandee.kinMel.KinMelCustomMessage;
import com.lagrandee.kinMel.bean.request.ProductRequest;
import com.lagrandee.kinMel.bean.response.ProductResponse;
import com.lagrandee.kinMel.bean.response.ResponseWithStatus;
import com.lagrandee.kinMel.bean.response.SingleDataResponse;
import com.lagrandee.kinMel.bean.response.SingleResponseWithStatus;
import com.lagrandee.kinMel.service.implementation.ProductServiceImplementation;
import jakarta.servlet.http.HttpServletRequest;
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

    @PreAuthorize("hasRole('Seller')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/products")
    public ResponseEntity<?> addProduct(
            @RequestPart("productDetails") ProductRequest productRequest,
            @RequestPart("productImages") MultipartFile[] productImages
            , HttpServletRequest request
    ) {
        String newProduct = productServiceImplementation.createNewProduct(productRequest, productImages,request);
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
        if (allProduct.isEmpty()){
            SingleResponseWithStatus r = new SingleResponseWithStatus();
            r.setStatus(HttpStatus.OK.value());
            r.setStatusValue(HttpStatus.OK.name());
            r.setData("No data found");
            return new ResponseEntity<>(r, HttpStatus.OK);
        }
        ResponseWithStatus response = new ResponseWithStatus();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.name());
        response.setData(allProduct);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("")
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getProductByProductId(@PathVariable int productId){
        ProductResponse specificProduct = productServiceImplementation.getProductById(productId);
        if (specificProduct==null){
            SingleResponseWithStatus r = new SingleResponseWithStatus();
            r.setStatus(HttpStatus.OK.value());
            r.setStatusValue(HttpStatus.OK.name());
            r.setData("No data found");
            return new ResponseEntity<>(r, HttpStatus.OK);
        }
        SingleDataResponse<ProductResponse> response = new SingleDataResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.name());
        response.setData(specificProduct);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("")
    @GetMapping ("/product")
    public  ResponseEntity<?> getProductByCategory(@RequestParam int categoryId){
        List<ProductResponse> productByCategoryId = productServiceImplementation.getProductByCategoryId(categoryId);
        SingleDataResponse<List<ProductResponse>> response = new SingleDataResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.name());
        response.setData(productByCategoryId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
