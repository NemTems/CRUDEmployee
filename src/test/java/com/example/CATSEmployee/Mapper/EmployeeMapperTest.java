package com.example.CATSEmployee.Mapper;

import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.mapper.EmployeeMapper;
import com.example.CATSEmployee.models.concrete.Department;
import com.example.CATSEmployee.models.concrete.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMapperTest {

    Employee employee;
    EmployeeDTO employeeDTO;
    Department department;

    @BeforeEach
    public void setUp() {
        department = Department.builder()
                .id(1)
                .cost_center_code("FIN001")
                .name("Finance")
                .build();

        employee = Employee.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .operational_head(true)
                .department(department)
                .build();

        employeeDTO = EmployeeDTO.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .operational_head(true)
                .department_id(1)
                .build();
    }

    @Test
    public void testToDto_NullEmployee() {
        EmployeeDTO employeeDTO = EmployeeMapper.toDto(null);
        Assertions.assertNull(employeeDTO);
    }

    @Test
    public void testToDto_ValidEmployee() {
        EmployeeDTO employeeDTO = EmployeeMapper.toDto(employee);

        Assertions.assertNotNull(employeeDTO);
        Assertions.assertEquals(employee.getId(), employeeDTO.getId());
        Assertions.assertEquals(employee.getFirstName(), employeeDTO.getFirstName());
        Assertions.assertEquals(employee.getLastName(), employeeDTO.getLastName());
        Assertions.assertEquals(employee.getEmail(), employeeDTO.getEmail());
        Assertions.assertEquals(employee.getOperational_head(), employeeDTO.getOperational_head());
        Assertions.assertEquals(department.getId(), employeeDTO.getDepartment_id());
    }

    @Test
    public void testToEntity_NullEmployeeDTO() {
        Employee employee = EmployeeMapper.toEntity(null);
        Assertions.assertNull(employee);
    }

    @Test
    public void testToEntity_ValidEmployeeDTO() {
        Employee employee = EmployeeMapper.toEntity(employeeDTO);

        Assertions.assertNotNull(employee);
        Assertions.assertEquals(employeeDTO.getId(), employee.getId());
        Assertions.assertEquals(employeeDTO.getFirstName(), employee.getFirstName());
        Assertions.assertEquals(employeeDTO.getLastName(), employee.getLastName());
        Assertions.assertEquals(employeeDTO.getEmail(), employee.getEmail());
        Assertions.assertEquals(employeeDTO.getOperational_head(), employee.getOperational_head());
        Assertions.assertEquals(employeeDTO.getDepartment_id(), employee.getDepartment().getId());
    }

    @Test
    public void testBasicEmployeeToDto_WithoutDepartment() {
        Employee subordinate = Employee.builder()
                .id(2)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .operational_head(false)
                .build();

        List<Employee> subordinates = new ArrayList<>();
        subordinates.add(subordinate);
        employee.setSubordinates(subordinates);

        EmployeeDTO employeeDTO = EmployeeMapper.toDto(employee);

        Assertions.assertNotNull(employeeDTO);
        Assertions.assertFalse(CollectionUtils.isEmpty(employeeDTO.getSubordinates()));
        Assertions.assertEquals(subordinate.getId(), employeeDTO.getSubordinates().get(0).getId());
    }

    @Test
    public void testBasicEmployeeToDto_WithDepartment() {
        Employee subordinate = Employee.builder()
                .id(2)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .operational_head(false)
                .department(department)
                .build();

        List<Employee> subordinates = new ArrayList<>();
        subordinates.add(subordinate);
        employee.setSubordinates(subordinates);

        EmployeeDTO employeeDTO = EmployeeMapper.toDto(employee);

        Assertions.assertNotNull(employeeDTO);
        Assertions.assertFalse(CollectionUtils.isEmpty(employeeDTO.getSubordinates()));
        Assertions.assertEquals(subordinate.getId(), employeeDTO.getSubordinates().getFirst().getId());
        Assertions.assertEquals(employeeDTO.getSubordinates().getFirst().getDepartment_id(), department.getId());
    }

    @Test
    public void testToEntity_EmployeeDTOWithoutSubordinates() {
        EmployeeDTO subordinateDTO = EmployeeDTO.builder()
                .id(2)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .operational_head(false)
                .build();

        List<EmployeeDTO> subordinates = new ArrayList<>();
        subordinates.add(subordinateDTO);
        employeeDTO.setSubordinates(subordinates);

        Employee employee = EmployeeMapper.toEntity(employeeDTO);

        Assertions.assertNotNull(employee);
        Assertions.assertFalse(CollectionUtils.isEmpty(employee.getSubordinates()));
        Assertions.assertEquals(subordinateDTO.getId(), employee.getSubordinates().getFirst().getId());
    }

    @Test
    public void testToEntity_EmployeeDTOWithSubordinates() {
        EmployeeDTO subordinateDTO = EmployeeDTO.builder()
                .id(2)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .department_id(1)
                .operational_head(false)
                .build();

        List<EmployeeDTO> subordinates = new ArrayList<>();
        subordinates.add(subordinateDTO);
        employeeDTO.setSubordinates(subordinates);

        Employee employee = EmployeeMapper.toEntity(employeeDTO);

        Assertions.assertNotNull(employee);
        Assertions.assertFalse(CollectionUtils.isEmpty(employee.getSubordinates()));
        Assertions.assertEquals(subordinateDTO.getId(), employee.getSubordinates().getFirst().getId());
        Assertions.assertEquals(employee.getSubordinates().getFirst().getDepartment().getId(), department.getId());
    }

    @Test
    public void testToDto_EmployeeWithDirectSupervisor() {
        Employee supervisor = Employee.builder()
                .id(2)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .operational_head(true)
                .build();

        employee.setDirect_supervisor(supervisor);

        EmployeeDTO employeeDTO = EmployeeMapper.toDto(employee);

        Assertions.assertNotNull(employeeDTO);
        Assertions.assertEquals(supervisor.getId(), employeeDTO.getDirect_supervisor_id());
    }

    @Test
    public void testToEntity_EmployeeDTOWithDirectSupervisor() {
        EmployeeDTO supervisorDTO = EmployeeDTO.builder()
                .id(2)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .operational_head(true)
                .build();

        employeeDTO.setDirect_supervisor_id(supervisorDTO.getId());

        Employee employee = EmployeeMapper.toEntity(employeeDTO);

        Assertions.assertNotNull(employee);
        Assertions.assertNotNull(employee.getDirect_supervisor());
        Assertions.assertEquals(supervisorDTO.getId(), employee.getDirect_supervisor().getId());
    }
}
