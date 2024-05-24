package com.lagrandee.kinMel.bean.response;

import lombok.Data;

import java.util.List;

@Data
public class SingleDataResponse<T> {
    private Integer status;
    private String statusValue;
    private T data;
}
