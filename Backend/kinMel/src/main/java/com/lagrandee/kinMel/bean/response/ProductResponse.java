package com.lagrandee.kinMel.bean.response;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data

public class ProductResponse {
    private Integer productId;
    private String productName;
    private String productDescription;
    private String brand;
    private String categoryName;
    private Long price;
    private Long discountedPrice;
    private Integer stockQuantity;
    private int productStatus;
    private Date createdAt;
    private Date updatedAt;
    private int featured;
    private List<String> productImages;
}
