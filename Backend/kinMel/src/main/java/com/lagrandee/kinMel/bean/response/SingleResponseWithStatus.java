package com.lagrandee.kinMel.bean.response;

import lombok.Data;

@Data
public class SingleResponseWithStatus {
    private Integer status;
    private String statusValue;
    private String data;
}
