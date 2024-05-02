package com.lagrandee.kinMel.service;

import com.lagrandee.kinMel.bean.UserDetail;
import com.lagrandee.kinMel.bean.request.UsersRegisterDTO;
import com.lagrandee.kinMel.entity.Role;
import com.lagrandee.kinMel.entity.Users;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface UserService {

Users getSpecificUserById(int id);

    List<UserDetail> getAllUsers();

    Users addOrUpdateUser(Users users);

    void removeUser(int id);

    ResponseEntity<?> registerUser(UsersRegisterDTO usersRegisterDTO);
    UserDetail getUserWithRole(int id);

}
