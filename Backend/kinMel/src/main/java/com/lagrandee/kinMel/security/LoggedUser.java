package com.lagrandee.kinMel.security;

import com.lagrandee.kinMel.entity.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoggedUser {
public static synchronized Users findUser(){
    Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            Users users=(Users) authentication.getPrincipal();
    return users;
}
}
