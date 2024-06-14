package com.lagrandee.kinMel.bean.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private int category_id;
    private String category_name;
    private String category_description;
    private String imagePath;
}
