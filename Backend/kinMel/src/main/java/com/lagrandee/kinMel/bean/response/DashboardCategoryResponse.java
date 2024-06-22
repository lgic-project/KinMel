package com.lagrandee.kinMel.bean.response;

import lombok.Data;

@Data
public class DashboardCategoryResponse {
    private String categoryName;
    private int noOfProducts;
    private int totalOrdersInEachCategory;

}
