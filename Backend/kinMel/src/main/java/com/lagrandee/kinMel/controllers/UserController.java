package com.lagrandee.kinMel.controllers;


import com.lagrandee.kinMel.KinMelCustomMessage;
import com.lagrandee.kinMel.bean.UserDetail;
import com.lagrandee.kinMel.bean.request.UsersRegisterDTO;
import com.lagrandee.kinMel.entity.Role;
import com.lagrandee.kinMel.entity.Users;
import com.lagrandee.kinMel.service.implementation.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.time.LocalDateTime;
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

//    @PostMapping("/users/register")
//    public Users addOrUpdateStudent(@RequestBody UsersRegisterDTO users){
////        users.setId(0);
////        return userServiceImplementation.addOrUpdateUser(users);
//        return new Users();
//    }

    @PreAuthorize("hasAnyRole()")
    @PostMapping("/users/register")
    public ResponseEntity<?> register(@RequestBody UsersRegisterDTO usersRegisterDTO){
    return new ResponseEntity<>(userServiceImplementation.registerUser(usersRegisterDTO),HttpStatus.OK);
    }


    //for update
    @PreAuthorize("hasAnyRole()")
    @PutMapping("/users/{userId}")
    public ResponseEntity<?> update(@PathVariable int userId, @RequestBody UsersRegisterDTO usersRegisterDTO){
        return new ResponseEntity<>(userServiceImplementation.updateUser(userId,usersRegisterDTO),HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole()")
    @PutMapping("/verify-account" )
    public ResponseEntity<String> verifyAccount(@RequestParam String email,@RequestParam String otp){
        return new ResponseEntity<>(userServiceImplementation.verifyAccount(email,otp),HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole()")
    @PutMapping("/regenerate-otp" )
    public ResponseEntity<String> regenerateOTP(@RequestParam String email){
        return new ResponseEntity<>(userServiceImplementation.regenerateOTP(email),HttpStatus.OK);
    }

}
