package com.lagrandee.kinMel.bean.response;

import lombok.Data;

import java.util.List;
@Data
public class ResponseWithStatus {
    private Integer status;
    private String statusValue;
    private List<?> data;
}
