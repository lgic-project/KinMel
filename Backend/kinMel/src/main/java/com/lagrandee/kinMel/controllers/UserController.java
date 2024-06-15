package com.lagrandee.kinMel.controllers;


import com.lagrandee.kinMel.bean.UserDetail;
import com.lagrandee.kinMel.bean.request.PasswordRequest;
import com.lagrandee.kinMel.bean.request.UsersRegisterDTO;
import com.lagrandee.kinMel.bean.response.SingleResponseWithStatus;
import com.lagrandee.kinMel.service.fileupload.FileUploadService;
import com.lagrandee.kinMel.service.implementation.UserServiceImplementation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public List<UserDetail> getAllUsers(){
    return  userServiceImplementation.getAllUsers();


    }

//    @GetMapping("/users/{userId}")
//    public UsersWithRoles getUserById(@PathVariable int userId){
//        UsersWithRoles userWithRole = userServiceImplementation.getUserWithRole(userId);
//        return userWithRole;
//    }
    @GetMapping("/user/{userId}")
    public UserDetail getUserById(@PathVariable int userId){
        UserDetail userWithRole = userServiceImplementation.getUserWithRole(userId);
//        Users userWithRole = userServiceImplementation.getSpecificUserById(userId);

        return userWithRole;
    }

    @PreAuthorize("hasAnyRole()")
    @GetMapping ("/users/role/{roleId}")
    public ResponseEntity<?> getAllUserByRole(@PathVariable int roleId){
        List<UserDetail> usersWithRole = userServiceImplementation.getUsersWithRole(roleId);
        return new ResponseEntity<>(usersWithRole,HttpStatus.OK);
    }


    @GetMapping("/user")
    public UserDetail getUserById(HttpServletRequest request){

        UserDetail userWithRole = userServiceImplementation.getUserWithRoleByToken(request);
//        Users userWithRole = userServiceImplementation.getSpecificUserById(userId);

        return userWithRole;
    }

    @PreAuthorize("hasAnyRole()")
    @PostMapping("/users/register")
    public ResponseEntity<?> register(@RequestBody UsersRegisterDTO usersRegisterDTO){
    return new ResponseEntity<>(userServiceImplementation.registerUser(usersRegisterDTO),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/users/block/{userId}")
    public  ResponseEntity<?> block (@PathVariable int userId){
        String blockuser = userServiceImplementation.blockuser(userId);
        return new ResponseEntity<>(blockuser,HttpStatus.OK);
    }



    //for update
//    @PreAuthorize("hasAnyRole()")
    @PreAuthorize("hasAnyRole()")
    @PutMapping("/users")
    public ResponseEntity<?> update( @RequestBody UsersRegisterDTO usersRegisterDTO, HttpServletRequest request){
        return new ResponseEntity<>(userServiceImplementation.updateUser(usersRegisterDTO,request),HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole()")
    @PutMapping("/verify-account" )
    public ResponseEntity<String> verifyAccount(@RequestParam String email,@RequestParam String otp){
        return new ResponseEntity<>(userServiceImplementation.verifyAccount(email,otp),HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole()")
    @PutMapping("/users/changepassword")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordRequest passwordRequest, HttpServletRequest request){
        String returnType = userServiceImplementation.updatePassword(passwordRequest, request);
        SingleResponseWithStatus response = new SingleResponseWithStatus();
        response.setStatus(HttpStatus.OK.value());
        response.setStatusValue(HttpStatus.OK.name());
        response.setData(returnType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole()")
    @PutMapping("/regenerate-otp" )
    public ResponseEntity<String> regenerateOTP(@RequestParam String email){
        return new ResponseEntity<>(userServiceImplementation.regenerateOTP(email),HttpStatus.OK);
    }



}
