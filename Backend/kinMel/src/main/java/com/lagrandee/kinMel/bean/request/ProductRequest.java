package com.lagrandee.kinMel.bean.request;

import lombok.Data;

@Data
public class ProductRequest {
    private String productName;
    private String productDescription;
    private String brand;
    private Integer categoryId;
    private Integer price;
    private Integer discountedPrice;
    private Integer stockQuantity;
    private int productStatus;
    private int featured;
}
