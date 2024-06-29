package com.lagrandee.kinMel.controllers;
import com.lagrandee.kinMel.bean.request.Order;
import com.lagrandee.kinMel.bean.response.OrderDeliverResponse;
import com.lagrandee.kinMel.bean.response.OrderResponse;
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

    @PreAuthorize("hasRole('Customer')")
    @PostMapping("/order")
    public ResponseEntity<?> placeOrder(@RequestBody Order order, @RequestParam List<Integer>cartIds, HttpServletRequest request) {
        String message = orderServiceImplementation.placeOrder(order, cartIds, request);
        SingleDataResponse<String> response = new SingleDataResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.name());
        response.setData(message);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @PreAuthorize("hasRole('Customer')")
    @GetMapping("/order")
    public ResponseEntity<?> getAllOrderOfUser(HttpServletRequest request){
        List<OrderResponse> orderOfUser = orderServiceImplementation.getOrderOfUser(request);
        if (orderOfUser.isEmpty()){
            SingleDataResponse<String> response=new SingleDataResponse<>();
            response.setStatus(HttpStatus.OK.value());
            response.setStatusValue(HttpStatus.OK.toString());
            response.setData("No data found");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        SingleDataResponse<List<OrderResponse>> response=new SingleDataResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.toString());
        response.setData(orderOfUser);
        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @PreAuthorize("hasRole('Seller')")
    @GetMapping("/orders")
    public ResponseEntity<?> getUnDeliveredOrderOfSeller(HttpServletRequest request){
        List<OrderDeliverResponse> unDeliverOrderOfSellerById = orderServiceImplementation.getUnDeliverOrderOfSellerById(request);
        SingleDataResponse<List<OrderDeliverResponse>> response=new SingleDataResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.toString());
        response.setData(unDeliverOrderOfSellerById);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}
