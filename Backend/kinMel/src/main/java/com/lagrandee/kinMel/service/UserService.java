package com.lagrandee.kinMel.service;

import com.lagrandee.kinMel.bean.UserDetail;
import com.lagrandee.kinMel.bean.request.UsersRegisterDTO;
import com.lagrandee.kinMel.entity.Users;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface UserService {

Users getSpecificUserById(int id);

    List<UserDetail> getAllUsers();


    ResponseEntity<?> registerUser(UsersRegisterDTO usersRegisterDTO);
    UserDetail getUserWithRole(int id);

}
