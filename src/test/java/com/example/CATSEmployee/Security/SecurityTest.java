package com.example.CATSEmployee.Security;

import com.example.CATSEmployee.controller.DepartmentController;
import com.example.CATSEmployee.models.concrete.Department;
import com.example.CATSEmployee.security.jwt.JwtUtils;
import com.example.CATSEmployee.service.interfaces.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDetails exampleUser;
    private UserDetails adminUser;

    @BeforeEach
    public void setup() {
        jwtUtils.setJwtSecret("superSecret1414fdiajfl392jf34124ohjfa983ufhdrhdr4h55634hd");
        jwtUtils.setJwtExpirationMs(100000);

        exampleUser = User.withUsername("exampleUser")
                .password("examplePassword")
                .roles("USER")
                .build();

        adminUser = User.withUsername("admin")
                .password("adminPassword")
                .roles("ADMIN")
                .build();

    }

    @Test
    public void notRegisteredUserInTheSystem_ShouldReturnUnauthorized() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/department/showAll")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, ""));

        response.andExpect(status().isUnauthorized());
    }

    @Test
    public void registeredUserInTheSystem_DefaultUser_ShouldReturnOk() throws Exception {

        ResultActions response = mockMvc.perform(get("/api/department/showAll")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtils.generateTokenFromUsername(exampleUser)));

        response.andExpect(status().isOk());
    }

    @Test
    public void registeredUserInTheSystem_DefaultUser_ShouldReturnForbiddenError() throws Exception {

        ResultActions response = mockMvc.perform(post("/api/department/create")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtils.generateTokenFromUsername(exampleUser)));

        response.andExpect(status().isForbidden());
    }

    @Test
    public void registeredUserInTheSystem_AdminUser_ShouldReturnOk() throws Exception {

        Department department = Department.builder()
                .id(1)
                .cost_center_code("FIN001")
                .name("Finance")
                .build();

        ResultActions response = mockMvc.perform(post("/api/department/create")
                        .content(objectMapper.writeValueAsString(department))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtils.generateTokenFromUsername(adminUser)));

        response.andExpect(status().isCreated());
    }
}
