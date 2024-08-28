package com.example.CATSEmployee.Security;

import com.example.CATSEmployee.TestSecurityConfig;
import com.example.CATSEmployee.security.AuthController;
import com.example.CATSEmployee.security.AuthServiceImpl;
import com.example.CATSEmployee.security.jwt.LoginRequest;
import com.example.CATSEmployee.security.jwt.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthServiceImpl authService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequest loginRequest;
    private LoginResponse loginResponse;

    @BeforeEach
    public void setup() {
        loginRequest = LoginRequest.builder()
                .username("exampleUser")
                .password("examplePassword")
                .build();

        loginResponse = LoginResponse.builder()
                .username("exampleUser")
                .jwtToken("eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNDA3MTgxNywiZXhwIjoxNzI0MDc1NDE3fQ.xpqR0kVluZQzHKQpSo_jnYSh6dyW46W-73aQbm4FALDbAY8Iaj1MzRMjeHDA5TP4")
                .roles(List.of("ROLE_USER"))
                .build();
    }

    @Test
    public void loginUserToTheSystem_ShouldLoginAndReturnResponse() throws Exception {

        when(authService.login(Mockito.any(LoginRequest.class))).thenReturn(loginResponse);
        ResultActions response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(objectMapper.writeValueAsString(loginRequest))));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", CoreMatchers.is(loginResponse.getUsername())))
                .andExpect(jsonPath("$.jwtToken", CoreMatchers.is(loginResponse.getJwtToken())))
                .andExpect(jsonPath("$.roles", CoreMatchers.is(loginResponse.getRoles())));
    }

    @Test
    public void registerUserInTheSystem_ShouldRegisterAndReturnSuccess() throws Exception {
        loginRequest.setUsername("exampleUser1");

        String successRegister = "User " + loginRequest.getUsername() + " registered successfully";
        when(authService.register(Mockito.any(LoginRequest.class))).thenReturn(successRegister);
        ResultActions response = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(objectMapper.writeValueAsString(loginRequest))));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$", CoreMatchers.is(successRegister)));

    }
}
