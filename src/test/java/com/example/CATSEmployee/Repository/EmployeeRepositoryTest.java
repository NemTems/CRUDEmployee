package com.example.CATSEmployee.Repository;

import com.example.CATSEmployee.models.concrete.Employee;
import com.example.CATSEmployee.models.concrete.Employee;
import com.example.CATSEmployee.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EmployeeRepositoryTest {
    @Autowired
    EmployeeRepository employeeRepository;

    Employee employeeOne, employeeTwo;

    @BeforeEach
    public void setUp(){
        employeeOne = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        employeeTwo = Employee.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .operational_head(false)
                .build();
    }

    @Test
    public void EmployeeRepository_SaveEmployeeTest_Saved(){
        Employee savedEmployee = employeeRepository.save(employeeOne);

        Assertions.assertNotNull(savedEmployee);
        Assertions.assertTrue(savedEmployee.getId() > 0);
    }

    @Test
    public void employeeRepository_FindAllDepartmentsTest_AllDepartment(){
        employeeRepository.save(employeeOne);
        employeeRepository.save(employeeTwo);

        List<Employee> departmentList = employeeRepository.findAll();

        Assertions.assertNotNull(departmentList);
        Assertions.assertEquals(employeeOne, departmentList.getFirst());
        Assertions.assertEquals(employeeTwo, departmentList.getLast());
    }

    @Test
    public void employeeRepository_FindDepartmentByIdTest_OneDepartment(){
        employeeRepository.save(employeeOne);
        Employee savedDepartment = employeeRepository.save(employeeTwo);

        Employee foundDepartment = employeeRepository.findById(savedDepartment.getId()).get();

        Assertions.assertNotNull(foundDepartment);
        Assertions.assertEquals(savedDepartment.getId(), foundDepartment.getId());
        Assertions.assertEquals(savedDepartment.getFirstName(), foundDepartment.getFirstName());
    }

    @Test
    public void employeeRepository_DeleteDepartmentByIdTest_Nothing(){
        employeeRepository.save(employeeOne);
        Employee savedDepartment = employeeRepository.save(employeeTwo);

        Assertions.assertTrue(employeeRepository.findById(savedDepartment.getId()).isPresent());

        employeeRepository.deleteById(savedDepartment.getId());

        Assertions.assertFalse(employeeRepository.findById(savedDepartment.getId()).isPresent());
    }
}
