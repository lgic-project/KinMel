package com.lagrandee.kinMel.bean.response;

import lombok.Data;

@Data
public class OrderDeliverResponse {
    private int eachItemOrderId;
    private int groupOrderId;
    private String productName;
    private int quantity;
    private int totalPrice;
    private String orderStatus;
    private String orderPersonName;
    private String orderPersonAddress;
    private String orderPersonPhoneNumber;
    private String paymentMethod;
    private String imagePath;
}
