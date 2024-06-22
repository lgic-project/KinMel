package com.lagrandee.kinMel.bean.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RatingRequest {
    private int productId;
    private BigDecimal rating;
}
