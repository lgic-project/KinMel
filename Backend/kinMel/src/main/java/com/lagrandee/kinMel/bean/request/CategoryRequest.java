package com.lagrandee.kinMel.bean.request;

import lombok.Data;

@Data
public class CategoryRequest {

    private String categoryName;
    private String categoryDescription;
    private String categoryImage;
    private String imageFormat;
}
