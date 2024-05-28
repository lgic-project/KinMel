package com.lagrandee.kinMel.bean.request;

import lombok.Data;

@Data
public class PasswordRequest {
    String oldPassword;
    String newPassword;
}
