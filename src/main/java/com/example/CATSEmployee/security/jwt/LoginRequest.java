package com.example.CATSEmployee.security.jwt;

import lombok.*;

@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;
}
