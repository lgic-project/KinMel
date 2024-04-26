package com.lagrandee.kinMel.controllers;


import com.lagrandee.kinMel.Repository.UsersRepository;
import com.lagrandee.kinMel.bean.request.LoginRequest;
import com.lagrandee.kinMel.bean.response.JwtResponse;
import com.lagrandee.kinMel.entity.Users;
import com.lagrandee.kinMel.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UsersRepository usersRepository;
    @GetMapping("/admin")
    @PreAuthorize("hasRole('Admin')")
    public String adminPage() {
        return "Admin Page";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('User')")
    public String userPage() {
        return "User Page";
    }
    @GetMapping("/auth/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Users users=usersRepository.findByEmail(loginRequest.getUserName());
        if (users.getActive()==1) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
            Users userDetails = (Users) authentication.getPrincipal();
            String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());
            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            String userId = userDetails.getEmail();
            String refreshToken = jwtUtils.generateJwtRefreshToken(userDetails.getUsername());

            return ResponseEntity.ok(new JwtResponse(jwt, refreshToken, userId, roles));
        }
        else {
            return new ResponseEntity<>("Account is blocked or not verified",HttpStatus.FORBIDDEN);
        }
    }
}
