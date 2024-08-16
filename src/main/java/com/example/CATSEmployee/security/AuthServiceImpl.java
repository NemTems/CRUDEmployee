package com.example.CATSEmployee.security;

import com.example.CATSEmployee.exception.APIRequestException;
import com.example.CATSEmployee.security.jwt.JwtUtils;
import com.example.CATSEmployee.security.jwt.LoginRequest;
import com.example.CATSEmployee.security.jwt.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final DataSource dataSource;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(JwtUtils jwtUtils, AuthenticationManager authenticationManager, DataSource dataSource, PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new APIRequestException("Bad credentials", e);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new LoginResponse(userDetails.getUsername(), jwtToken, roles);
    }

    @Override
    public String register(LoginRequest loginRequest) {
        UserDetails newUser = User.withUsername(loginRequest.getUsername())
                .password(passwordEncoder.encode(loginRequest.getPassword()))
                .roles("USER")
                .build();

        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        if(userDetailsManager.userExists(newUser.getUsername())){
            throw new APIRequestException("Username already exists");
        }

        userDetailsManager.createUser(newUser);
        return "User " + newUser.getUsername() + " registered successfully";
    }
}
