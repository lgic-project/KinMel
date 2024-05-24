package com.lagrandee.kinMel.controllers;
import com.lagrandee.kinMel.Repository.UsersRepository;
import com.lagrandee.kinMel.bean.request.LoginRequest;
import com.lagrandee.kinMel.bean.response.JwtResponse;
import com.lagrandee.kinMel.entity.Users;
import com.lagrandee.kinMel.exception.UserNotVerified;
import com.lagrandee.kinMel.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
@RestController()
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UsersRepository usersRepository;

    @GetMapping("/kinMel/login")
    public ResponseEntity<?> authenticateUser(@RequestParam  String username,String password ) {
        Users users=usersRepository.findByEmail(username);
        if (users.getActive()==1) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            Users userDetails = (Users) authentication.getPrincipal();
            String jwt = jwtUtils.generateJwtToken(userDetails.getUsername(), userDetails.getUserId());
            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            String userId = userDetails.getEmail();
            System.out.println(jwt);
            String refreshToken = jwtUtils.generateJwtRefreshToken(userDetails.getUsername());

            return ResponseEntity.ok(new JwtResponse(200,jwt, refreshToken, userId, roles));
        }
        else {
            throw new UserNotVerified("User is not verified");
        }
    }
}
