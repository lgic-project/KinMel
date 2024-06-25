package com.lagrandee.kinMel.bean.request;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UsersRegisterDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private Long phoneNumber;
    private Integer role;
    private String profilePhoto;
    private String imageFormat;


}
