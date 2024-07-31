package com.example.CATSEmployee.Controller;

import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.controller.EmployeeController;
import com.example.CATSEmployee.service.implementations.EmployeeServiceImpl;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeServiceImpl employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    EmployeeDTO employeeDTO;

    @BeforeEach
    public void setUp(){
        employeeDTO = EmployeeDTO.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .operational_head(true)
                .build();
    }

    @Test
    public void showAllEmployees_ShouldReturnAllEmployees() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(List.of(employeeDTO));
        ResultActions response = mockMvc.perform(get("/api/employee/showAll")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", CoreMatchers.is(employeeDTO.getFirstName())));
    }

    @Test
    public void showEmployee_ShouldReturnEmployeeById() throws Exception{
        when(employeeService.getEmployeeById(1)).thenReturn(employeeDTO);
        ResultActions response = mockMvc.perform(get("/api/employee/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employeeDTO.getFirstName())));
    }

    @Test
    public void createEmployee_ShouldCreateEmployee() throws Exception {
        when(employeeService.createEmployee(Mockito.any(EmployeeDTO.class)))
                .thenReturn(employeeDTO);

        ResultActions response = mockMvc.perform(post("/api/employee/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employeeDTO.getFirstName())));
    }

    @Test
    public void updateEmployee_ShouldReturnUpdatedEmployee() throws Exception {
        when(employeeService.updateEmployee(Mockito.any(EmployeeDTO.class), Mockito.anyInt()))
                .thenReturn(employeeDTO);

        ResultActions response = mockMvc.perform(patch("/api/employee/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employeeDTO.getFirstName())));
    }

    @Test
    public void addSubordinatesToEmployee_ShouldReturnUpdatedEmployee() throws Exception {
        EmployeeDTO subordinateDTO = EmployeeDTO.builder()
                .id(2)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .operational_head(false)
                .build();

        List<EmployeeDTO> subordinates = List.of(subordinateDTO);

        when(employeeService.addSubordinates(Mockito.anyList(), Mockito.anyInt()))
                .thenReturn(employeeDTO);

        ResultActions response = mockMvc.perform(put("/api/employee/1/add/subordinates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subordinates)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employeeDTO.getFirstName())));
    }

    @Test
    public void removeSubordinateFromEmployee_ShouldReturnUpdatedEmployee() throws Exception {
        EmployeeDTO subordinateDTO = EmployeeDTO.builder()
                .id(2)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .operational_head(false)
                .build();

        List<EmployeeDTO> subordinates = List.of(subordinateDTO);

        when(employeeService.removeSubordinates(Mockito.anyList(), Mockito.anyInt()))
                .thenReturn(employeeDTO);

        ResultActions response = mockMvc.perform(put("/api/employee/1/remove/subordinates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subordinates)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employeeDTO.getFirstName())));
    }

    @Test
    public void deleteEmployee_ShouldReturnNothing() throws Exception{
        doNothing().when(employeeService).deleteEmployeeById(Mockito.anyInt());

        ResultActions response = mockMvc.perform(delete("/api/employee/1/delete")
                .contentType(MediaType.APPLICATION_JSON));

        verify(employeeService, times(1)).deleteEmployeeById(Mockito.anyInt());

        response.andExpect(status().isOk());
    }
}
