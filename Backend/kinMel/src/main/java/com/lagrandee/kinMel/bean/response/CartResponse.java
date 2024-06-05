package com.lagrandee.kinMel.bean.response;

import lombok.Data;

@Data
public class CartResponse {
    private Integer cartId;
    private String productImagePath;
    private String productName;
    private Integer price;
    private Integer discountedPrice;
    private Integer quantity;
    private  Integer total;
}
