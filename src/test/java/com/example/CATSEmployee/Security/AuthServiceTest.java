package com.example.CATSEmployee.Security;

import com.example.CATSEmployee.exception.APIRequestException;
import com.example.CATSEmployee.security.AuthServiceImpl;
import com.example.CATSEmployee.security.jwt.JwtUtils;
import com.example.CATSEmployee.security.jwt.LoginRequest;
import com.example.CATSEmployee.security.jwt.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @Mock
    private JdbcUserDetailsManager userDetailsManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
                .username("exampleUser")
                .password("examplePassword")
                .build();
    }

    @Test
    void login_Success() {

        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("exampleUser");
        when(userDetails.getAuthorities()).thenReturn(List.of());
        when(jwtUtils.generateTokenFromUsername(userDetails)).thenReturn("mockedJwtToken");

        LoginResponse response = authService.login(loginRequest);

        assertEquals("exampleUser", response.getUsername());
        assertEquals("mockedJwtToken", response.getJwtToken());
        assertEquals(0, response.getRoles().size());

        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void login_Failure_BadCredentials() {
        LoginRequest loginRequest = LoginRequest.builder()
                .username("exampleUser")
                .password("examplePassword")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Bad credentials") {});

        APIRequestException exception = assertThrows(APIRequestException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Bad credentials", exception.getMessage());
    }

    @Test
    public void register_Success(){
        loginRequest.setUsername("newUser");

        when(passwordEncoder.encode(loginRequest.getPassword())).thenReturn("encodedNewUserPassword");

        String response = authService.register(loginRequest);

        verify(userDetailsManager, times(1)).createUser(Mockito.any(User.class));
        verify(userDetailsManager, times(1)).userExists(Mockito.anyString());

        assertEquals("User " + loginRequest.getUsername() + " registered successfully", response);
    }

    @Test
    public void register_Failure_UsernameAlreadyExists(){
        loginRequest.setUsername("newUser");

        when(passwordEncoder.encode(loginRequest.getPassword())).thenReturn("encodedNewUserPassword");
        when(userDetailsManager.userExists(Mockito.anyString())).thenReturn(true);

        APIRequestException exception = assertThrows(APIRequestException.class, () -> {
            authService.register(loginRequest);
        });

        assertEquals("Username already exists", exception.getMessage());
    }
}
