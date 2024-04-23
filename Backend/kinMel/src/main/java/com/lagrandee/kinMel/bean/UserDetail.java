package com.lagrandee.kinMel.bean;

import lombok.Data;

import java.util.List;


@Data
public class UserDetail {
    private int userId;
    private String first_name;
    private String last_name;
    private List<String> roles;
    private String image;
    private String email;
    private String phoneNumber;

}
