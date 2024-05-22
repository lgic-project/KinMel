package com.lagrandee.kinMel.bean.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    int status;
String token;
String refreshToken;
String email;
List<String> roles;
}
