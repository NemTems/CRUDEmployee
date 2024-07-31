package com.example.CATSEmployee.Mapper;

import com.example.CATSEmployee.DTO.concrete.DepartmentDTO;
import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.mapper.DepartmentMapper;
import com.example.CATSEmployee.models.concrete.Department;
import com.example.CATSEmployee.models.concrete.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class DepartmentMapperTest {

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
    public void testToDto_NullDepartment() {
        DepartmentDTO departmentDTO = DepartmentMapper.toDto(null);
        Assertions.assertNull(departmentDTO);
    }

    @Test
    public void testToDto_ValidDepartment() {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);

        department.setEmployees(employees);

        DepartmentDTO departmentDTO = DepartmentMapper.toDto(department);

        Assertions.assertNotNull(departmentDTO);
        Assertions.assertEquals(department.getId(), departmentDTO.getId());
        Assertions.assertEquals(department.getCost_center_code(), departmentDTO.getCost_center_code());
        Assertions.assertEquals(department.getName(), departmentDTO.getName());
        Assertions.assertFalse(CollectionUtils.isEmpty(departmentDTO.getEmployees()));
    }

    @Test
    public void testEmployeeToDTO_NotValidEmployee(){
        department.setEmployees(null);

        DepartmentDTO departmentDTO = DepartmentMapper.toDto(department);

        Assertions.assertNotNull(departmentDTO);
        Assertions.assertEquals(department.getId(), departmentDTO.getId());
        Assertions.assertEquals(department.getCost_center_code(), departmentDTO.getCost_center_code());
        Assertions.assertEquals(department.getName(), departmentDTO.getName());
        Assertions.assertTrue(CollectionUtils.isEmpty(departmentDTO.getEmployees()));
    }

    @Test
    public void testEmployeeToDto_ValidDirectSupervisor() {

        Employee tempEmployee = Employee.builder()
                .id(1)
                .firstName("temp")
                .lastName("temp")
                .email("temp.temp@example.com")
                .operational_head(true)
                .build();

        employee.setDirect_supervisor(tempEmployee);

        List<Employee> employees = new ArrayList<>();
        employees.add(employee);

        department.setEmployees(employees);

        DepartmentDTO departmentDTO = DepartmentMapper.toDto(department);

        Assertions.assertNotNull(departmentDTO);
        Assertions.assertEquals(department.getId(), departmentDTO.getId());
        Assertions.assertEquals(department.getCost_center_code(), departmentDTO.getCost_center_code());
        Assertions.assertEquals(department.getName(), departmentDTO.getName());
        Assertions.assertEquals(1, departmentDTO.getEmployees().getFirst().getDirect_supervisor_id());
    }

    @Test
    public void testToEntity_NullDepartmentDTO() {
        Department department = DepartmentMapper.toEntity(null);
        Assertions.assertNull(department);
    }

    @Test
    public void testToEntity_ValidDepartmentDTO() {

        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        employeeDTOs.add(employeeDTO);

        departmentDTO.setEmployees(employeeDTOs);

        Department department = DepartmentMapper.toEntity(departmentDTO);

        Assertions.assertNotNull(department);
        Assertions.assertEquals(departmentDTO.getId(), department.getId());
        Assertions.assertEquals(departmentDTO.getCost_center_code(), department.getCost_center_code());
        Assertions.assertEquals(departmentDTO.getName(), department.getName());
        Assertions.assertFalse(CollectionUtils.isEmpty(department.getEmployees()));
    }

    @Test
    public void testEmployeeToEntity_NotValidEmployee(){

        departmentDTO.setEmployees(null);

        Department department = DepartmentMapper.toEntity(departmentDTO);

        Assertions.assertNotNull(department);
        Assertions.assertEquals(departmentDTO.getId(), department.getId());
        Assertions.assertEquals(departmentDTO.getCost_center_code(), department.getCost_center_code());
        Assertions.assertEquals(departmentDTO.getName(), department.getName());
        Assertions.assertTrue(CollectionUtils.isEmpty(department.getEmployees()));
    }
}
