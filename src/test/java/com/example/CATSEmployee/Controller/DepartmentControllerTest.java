package com.example.CATSEmployee.Controller;

import com.example.CATSEmployee.DTO.concrete.DepartmentDTO;
import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.controller.DepartmentController;
import com.example.CATSEmployee.models.concrete.Department;
import com.example.CATSEmployee.models.concrete.Employee;
import com.example.CATSEmployee.service.implementations.DepartmentServiceImpl;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = DepartmentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DepartmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentServiceImpl departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    Employee employee;
    Department department;

    EmployeeDTO employeeDTO;
    DepartmentDTO departmentDTO;

    @BeforeEach
    public void setUp(){
        employee = Employee.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .operational_head(true)
                .build();

        department = Department.builder()
                .id(1)
                .cost_center_code("FIN001")
                .name("Finance")
                .build();

        employeeDTO = EmployeeDTO.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .operational_head(true)
                .build();

        departmentDTO = DepartmentDTO.builder()
                .id(1)
                .cost_center_code("FIN001")
                .name("Finance")
                .build();
    }

    @Test
    public void showAllDepartments_ShouldReturnAllDepartments() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(List.of(departmentDTO));
        ResultActions response = mockMvc.perform(get("/api/department/showAll")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", CoreMatchers.is(departmentDTO.getName())));
    }

    @Test
    public void showDepartment_ShouldReturnDepartmentById() throws Exception{
        when(departmentService.getDepartmentById(1)).thenReturn(departmentDTO);
        ResultActions response = mockMvc.perform(get("/api/department/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentDTO)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(departmentDTO.getName())));
    }

    @Test
    public void createDepartment_ShouldCreateDepartment() throws Exception {
        when(departmentService.createDepartment(Mockito.any(DepartmentDTO.class)))
                .thenReturn(departmentDTO);

        ResultActions response = mockMvc.perform(post("/api/department/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(objectMapper.writeValueAsString(departmentDTO))));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(departmentDTO.getName())));
    }

    @Test
    public void addEmployee_ShouldReturnUpdatedDepartment() throws Exception {
        EmployeeDTO newEmployeeDTO = EmployeeDTO.builder()
                .id(1)
                .build();

        List<EmployeeDTO> employeeDTOList = List.of(newEmployeeDTO);

        departmentDTO.setEmployees(employeeDTOList);

        when(departmentService.addEmployee(Mockito.anyList(), Mockito.anyInt())).thenReturn(departmentDTO);

        ResultActions response = mockMvc.perform(put("/api/department/1/add/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(objectMapper.writeValueAsString(employeeDTOList))));

            response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(departmentDTO.getName())))
                .andExpect(jsonPath("$.employees[0].id", CoreMatchers.is(departmentDTO.getEmployees().getFirst().getId())));
    }

    @Test
    public void removeEmployee_ShouldReturnUpdatedDepartment() throws Exception {
        when(departmentService.removeEmployee(Mockito.anyList(), Mockito.anyInt())).thenReturn(departmentDTO);

        ResultActions response = mockMvc.perform(put("/api/department/1/remove/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(objectMapper.writeValueAsString(List.of(employeeDTO)))));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(departmentDTO.getName())))
                .andExpect(jsonPath("$.employees", CoreMatchers.nullValue()));
    }

    @Test
    public void updateDepartment_ShouldReturnUpdatedDepartment() throws Exception{

        DepartmentDTO newDepartment = DepartmentDTO.builder()
                .name("New Department")
                .cost_center_code("NWD202")
                .build();

        when(departmentService.updateDepartment(Mockito.any(DepartmentDTO.class),Mockito.anyInt())).thenReturn(newDepartment);

        ResultActions response = mockMvc.perform(put("/api/department/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDepartment)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name",  CoreMatchers.is(newDepartment.getName())));
    }

    @Test
    public void deleteDepartment_ShouldReturnNothing() throws Exception{

        doNothing().when(departmentService).deleteDepartmentById(Mockito.anyInt());

        ResultActions response = mockMvc.perform(delete("/api/department/1/delete")
                .contentType(MediaType.APPLICATION_JSON));

        verify(departmentService, times(1)).deleteDepartmentById(Mockito.anyInt());

        response.andExpect(status().isOk());
    }

}
