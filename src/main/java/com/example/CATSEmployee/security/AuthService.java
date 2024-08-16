package com.example.CATSEmployee.security;


import com.example.CATSEmployee.security.jwt.LoginRequest;
import com.example.CATSEmployee.security.jwt.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);

    String register(LoginRequest loginRequest);
}
