package com.lagrandee.kinMel.service;

import com.lagrandee.kinMel.bean.UserDetail;
import com.lagrandee.kinMel.entity.Role;
import com.lagrandee.kinMel.entity.Users;

import java.util.List;


public interface UserService {

Users getSpecificUserById(int id);

    List<Users> getAllUsers();

    Users addOrUpdateUser(Users users);

    void removeUser(int id);


    UserDetail getUserWithRole(int id);

}
