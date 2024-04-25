package com.lagrandee.kinMel.controllers;


import com.lagrandee.kinMel.bean.UserDetail;
import com.lagrandee.kinMel.entity.Role;
import com.lagrandee.kinMel.entity.Users;
import com.lagrandee.kinMel.service.implementation.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.List;

@RestController
@RequestMapping("/kinMel")
public class UserController {

    private final UserServiceImplementation userServiceImplementation;

    @Autowired
    public UserController(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }


    @GetMapping("/users")
    public List<Users> getAllUsers(){
    return  userServiceImplementation.getAllUsers();

    }

//    @GetMapping("/users/{userId}")
//    public UsersWithRoles getUserById(@PathVariable int userId){
//        UsersWithRoles userWithRole = userServiceImplementation.getUserWithRole(userId);
//        return userWithRole;
//    }

    @GetMapping("/users/{userId}")
    public UserDetail getUserById(@PathVariable int userId){
        UserDetail userWithRole = userServiceImplementation.getUserWithRole(userId);
//        Users userWithRole = userServiceImplementation.getSpecificUserById(userId);
        return userWithRole;
    }

    @PostMapping("/users/register")
    public Users addOrUpdateStudent(@RequestBody Users users){
        users.setId(0);
        return userServiceImplementation.addOrUpdateUser(users);
    }

}
