package com.lagrandee.kinMel.bean.response;

import lombok.Data;

@Data
public class OrderResponse {
    private Integer orderId;
    private String imagePath;
    private String productName;
    private Integer quantity;
    private Integer totalPrice;
    private String orderStatus;
    private String orderedAt;
}
