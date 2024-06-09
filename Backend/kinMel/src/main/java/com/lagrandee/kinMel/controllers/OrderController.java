package com.lagrandee.kinMel.controllers;
import com.lagrandee.kinMel.bean.request.Order;
import com.lagrandee.kinMel.bean.response.SingleDataResponse;
import com.lagrandee.kinMel.service.implementation.OrderServiceImplementation;
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
public class OrderController {
    private final OrderServiceImplementation orderServiceImplementation;

    @PreAuthorize("hasAnyRole('Customer')")
    @PostMapping("/order")
    public ResponseEntity<?> placeOrder(@RequestBody Order order, @RequestParam List<Integer>cartIds, HttpServletRequest request) {
        String message = orderServiceImplementation.placeOrder(order, cartIds, request);
        SingleDataResponse<String> response = new SingleDataResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.name());
        response.setData(message);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
