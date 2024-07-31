package com.example.CATSEmployee.Service;

import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.exception.APIRequestException;
import com.example.CATSEmployee.mapper.EmployeeMapper;
import com.example.CATSEmployee.models.concrete.Employee;
import com.example.CATSEmployee.repository.EmployeeRepository;
import com.example.CATSEmployee.service.implementations.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeDTO employeeDTO;
    private Employee employee;

    @BeforeEach
    public void setUp() {
        employeeDTO = EmployeeDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
    }

    @Test
    public void getAllEmployees_ShouldReturnAllEmployees_ListOfEmployees() {
        List<Employee> employees = List.of(employee);

        when(employeeRepository.findAll()).thenReturn(employees);

        List<EmployeeDTO> result = employeeService.getAllEmployees();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(employee.getFirstName(), result.getFirst().getFirstName());
    }

    @Test
    public void getEmployeeById_ShouldReturnEmployee_SpecificEmployee() {
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));

        EmployeeDTO foundEmployee = employeeService.getEmployeeById(1);

        Assertions.assertNotNull(foundEmployee);
        Assertions.assertEquals(employee.getFirstName(), foundEmployee.getFirstName());
    }

    @Test
    public void getEmployeeById_ShouldThrowException_WhenEmployeeNotFound() {
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(APIRequestException.class, () -> {
            employeeService.getEmployeeById(1);
        });

        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    public void createEmployee_ShouldCreateNewEmployee_DirectSupervisorZero_NewEmployee() {
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);

        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);

        Assertions.assertNotNull(createdEmployee);
        Assertions.assertEquals(employeeDTO.getFirstName(), createdEmployee.getFirstName());
        Assertions.assertEquals(true, createdEmployee.getOperational_head());
        Assertions.assertEquals(0, createdEmployee.getDirect_supervisor_id());
    }


    @Test
    public void createEmployee_ShouldCreateNewEmployee_DirectSupervisorNotZero_NewEmployee() {
        employee.setId(1);
        employee.setDirect_supervisor(employee);

        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));

        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);

        Assertions.assertNotNull(createdEmployee);
        Assertions.assertEquals(employeeDTO.getFirstName(), createdEmployee.getFirstName());
        Assertions.assertEquals(false, createdEmployee.getOperational_head());
        Assertions.assertEquals(1, createdEmployee.getDirect_supervisor_id());
    }

    @Test
    public void createEmployee_ShouldThrowException_WhenEmployeeSaveFails() {
        when(employeeRepository.save(any(Employee.class))).thenThrow(new DataIntegrityViolationException("Error saving employee"));

        APIRequestException exception = Assertions.assertThrows(APIRequestException.class, () -> {
            employeeService.createEmployee(employeeDTO);
        });

        Assertions.assertTrue(exception.getMessage().contains("Exception occurred on attempt to create an employee"));
    }

    @Test
    public void updateEmployee_ShouldUpdateEmployee_() {
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);

        EmployeeDTO newEmployee = EmployeeDTO.builder()
                .firstName("New")
                .lastName("Employee")
                .email("new.employee@example.com")
                .operational_head(false)
                .direct_supervisor_id(1)
                .department_id(1)
                .build();

        EmployeeDTO updatedEmployee = employeeService.updateEmployee(newEmployee, 1);

        Assertions.assertNotNull(updatedEmployee);
        Assertions.assertEquals(newEmployee.getFirstName(), updatedEmployee.getFirstName());
        Assertions.assertEquals(newEmployee.getOperational_head(), updatedEmployee.getOperational_head());
    }

    @Test
    public void updateEmployee_ShouldThrowException_WhenEmployeeSaveFails() {
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenThrow(new DataIntegrityViolationException("Error saving employee"));

        EmployeeDTO newEmployee = EmployeeDTO.builder().build();

        APIRequestException exception = Assertions.assertThrows(APIRequestException.class, () -> {
            employeeService.updateEmployee(newEmployee, 1);
        });

        Assertions.assertTrue(exception.getMessage().contains("Exception occurred on attempt to save employee with id:"));
    }

    @Test
    public void updateEmployee_ShouldThrowIllegalArgException_WhenWrongArgumentsPassed() {
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenThrow(new IllegalArgumentException("Wrong arguments"));

        EmployeeDTO newEmployee = EmployeeDTO.builder().build();

        APIRequestException exception = Assertions.assertThrows(APIRequestException.class, () -> {
            employeeService.updateEmployee(newEmployee, 1);
        });

        Assertions.assertTrue(exception.getMessage().contains("Wrong arguments were passed to update employee with id:"));
    }

    @Test
    public void deleteEmployeeById_ShouldDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(Mockito.anyInt());

        employeeService.deleteEmployeeById(1);

        verify(employeeRepository, times(1)).deleteById(1);
    }

    @Test
    public void deleteEmployeeById_ShouldThrowException_WhenDeleteFails() {
        doThrow(new DataIntegrityViolationException("Error deleting employee")).when(employeeRepository).deleteById(Mockito.anyInt());

        APIRequestException exception = Assertions.assertThrows(APIRequestException.class, () -> {
            employeeService.deleteEmployeeById(1);
        });

        Assertions.assertTrue(exception.getMessage().contains("Exception occurred on attempt to delete employee with id:"));
    }

    @Test
    public void addSubordinates_ShouldAddSubordinatesToEmployee_UpdatedEmployee() {
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);

        EmployeeDTO updatedEmployee = employeeService.addSubordinates(List.of(employeeDTO), 1);

        verify(employeeRepository, times(2)).findById(Mockito.anyInt());
        verify(employeeRepository, times(2)).save(Mockito.any(Employee.class));

        Assertions.assertNotNull(updatedEmployee);
        Assertions.assertFalse(updatedEmployee.getSubordinates().isEmpty());
    }

    @Test
    public void addSubordinates_ShouldThrowException_WhenSubordinateNotFound() {
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(APIRequestException.class, () -> {
            employeeService.addSubordinates(List.of(employeeDTO), 1);
        });

        verify(employeeRepository, times(1)).findById(Mockito.anyInt());
    }

    @Test
    public void addSubordinates_ShouldThrowException_DoesntHasSubordinate_WhenEmployeeSaveFails() {
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenThrow(new DataIntegrityViolationException("Error saving employee"));

        APIRequestException exception = Assertions.assertThrows(APIRequestException.class, () -> {
            employeeService.addSubordinates(List.of(employeeDTO), 1);
        });

        Assertions.assertTrue(exception.getMessage().contains("Exception occurred on attempt to save subordinate with id:"));
    }

    @Test
    public void addSubordinates_ShouldThrowException_HasSubordinate_WhenEmployeeSaveFails() {
        employee.setId(1);
        employee.setSubordinates(List.of(employee));

        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenThrow(new DataIntegrityViolationException("Error saving employee"));

        APIRequestException exception = Assertions.assertThrows(APIRequestException.class, () -> {
            employeeService.addSubordinates(List.of(employeeDTO), 1);
        });

        Assertions.assertTrue(exception.getMessage().contains("Exception occurred on attempt to save employee with id:"));
    }

    @Test
    public void removeSubordinates_ShouldRemoveSubordinatesFromEmployee_UpdatedEmployee() {
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);

        EmployeeDTO updatedEmployee = employeeService.removeSubordinates(List.of(employeeDTO), 1);

        Assertions.assertNotNull(updatedEmployee);
        Assertions.assertTrue(updatedEmployee.getSubordinates().isEmpty());
    }

    @Test
    public void removeSubordinates_ShouldThrowException_WhenSubordinateNotFound() {
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(APIRequestException.class, () -> {
            employeeService.removeSubordinates(List.of(employeeDTO), 1);
        });

        verify(employeeRepository, times(1)).findById(Mockito.anyInt());
    }

    @Test
    public void removeSubordinates_ShouldThrowException_DoesntHasSubordinate_WhenEmployeeSaveFails() {
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenThrow(new DataIntegrityViolationException("Error saving employee"));

        APIRequestException exception = Assertions.assertThrows(APIRequestException.class, () -> {
            employeeService.removeSubordinates(List.of(employeeDTO), 1);
        });

        Assertions.assertTrue(exception.getMessage().contains("Exception occurred on attempt to save subordinate with id:"));
    }

    @Test
    public void removeSubordinates_ShouldThrowException_HasSubordinate_WhenEmployeeSaveFails() {
        employee.setId(1);
        employee.setSubordinates(List.of(employee));

        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenThrow(new DataIntegrityViolationException("Error saving employee"));

        APIRequestException exception = Assertions.assertThrows(APIRequestException.class, () -> {
            employeeService.removeSubordinates(List.of(employeeDTO), 1);
        });

        Assertions.assertTrue(exception.getMessage().contains("Exception occurred on attempt to save employee with id:"));
    }

}
