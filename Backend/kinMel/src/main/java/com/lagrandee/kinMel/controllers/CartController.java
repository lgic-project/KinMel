package com.lagrandee.kinMel.controllers;

import com.lagrandee.kinMel.bean.response.SingleDataResponse;
import com.lagrandee.kinMel.service.implementation.CartServiceImplementation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kinMel")
@RequiredArgsConstructor
public class CartController {
    private final CartServiceImplementation cartServiceImplementation;


    @PreAuthorize("hasAnyRole('Customer')")
    @PostMapping("/carts")
    public ResponseEntity<?> createCart(@RequestParam int productId, HttpServletRequest request) {
        String newCart = cartServiceImplementation.createNewCart(productId, request);
        SingleDataResponse<String> response = new SingleDataResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.name());
        response.setData(newCart);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('Customer')")
    @PutMapping("/carts")
    public ResponseEntity<?> updateCart(@RequestParam int cartId,@RequestParam String quantityChange) {
        String newCart = cartServiceImplementation.updateCart(cartId,quantityChange);
        SingleDataResponse<String> response = new SingleDataResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.name());
        response.setData(newCart);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('Customer')")
    @DeleteMapping("/carts")
    public ResponseEntity<?> deleteCartItems(@RequestParam List<Integer> cartId) {
        String deleteMessage = cartServiceImplementation.deleteCart(cartId);
        SingleDataResponse<String> response = new SingleDataResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.name());
        response.setData(deleteMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
