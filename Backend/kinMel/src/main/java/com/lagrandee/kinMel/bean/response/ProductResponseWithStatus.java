package com.lagrandee.kinMel.bean.response;

import lombok.Data;

import java.util.List;
@Data
public class ProductResponseWithStatus {
    private Integer status;
    private String statusValue;
    private List<ProductResponse> products;
}
