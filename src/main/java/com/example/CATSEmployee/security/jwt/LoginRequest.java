package com.example.CATSEmployee.security.jwt;

import lombok.*;

@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
