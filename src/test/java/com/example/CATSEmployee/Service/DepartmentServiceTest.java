package com.example.CATSEmployee.Service;

import com.example.CATSEmployee.DTO.concrete.DepartmentDTO;
import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.exception.APIRequestException;
import com.example.CATSEmployee.models.concrete.Department;
import com.example.CATSEmployee.models.concrete.Employee;
import com.example.CATSEmployee.repository.DepartmentRepository;
import com.example.CATSEmployee.repository.EmployeeRepository;
import com.example.CATSEmployee.service.implementations.DepartmentServiceImpl;
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
public class DepartmentServiceTest {
    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private DepartmentDTO departmentDTO;

    private Department department;

    private EmployeeDTO employeeDTO;

    private Employee employee;

    @BeforeEach
    public void setUp(){
        departmentDTO = DepartmentDTO.builder()
                .name("Finance")
                .cost_center_code("FIN001")
                .build();

        department = Department.builder()
                .name("Finance")
                .cost_center_code("FIN001")
                .build();

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
    public void getAllDepartments_ShouldReturnAllDepartments_ListOfDepartments(){
        List<Department> departmentList = List.of(department);

        when(departmentRepository.findAll()).thenReturn(departmentList);

        List<DepartmentDTO> departmentDTOList = departmentService.getAllDepartments();

        Assertions.assertNotNull(departmentDTOList);
        Assertions.assertEquals(department.getId(), departmentDTOList.getFirst().getId());
        Assertions.assertEquals(department.getName(), departmentDTOList.getFirst().getName());
    }

    @Test
    public void getDepartmentById_ShouldReturnDepartment_SpecificDepartment(){
        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(department));

        DepartmentDTO foundDepartment = departmentService.getDepartmentById(1);

        Assertions.assertNotNull(foundDepartment);
        Assertions.assertEquals(department.getName(), foundDepartment.getName());
    }

    @Test
    public void getDepartmentById_ShouldThrowException_WhenEmployeeNotFound() {
        when(departmentRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(APIRequestException.class, () -> {
            departmentService.getDepartmentById(1);
        });

        verify(departmentRepository, times(1)).findById(1);
    }

    @Test
    public void createDepartment_ShouldCreateNewDepartment_NewDepartment(){
        when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(department);

        DepartmentDTO createdDepartment = departmentService.createDepartment(departmentDTO);

        Assertions.assertNotNull(createdDepartment);
        Assertions.assertEquals(departmentDTO.getName(), createdDepartment.getName());
    }


    @Test
    public void createDepartment_ShouldThrowException_WhenDepartmentSaveFails() {
        when(departmentRepository.save(any(Department.class))).thenThrow(new DataIntegrityViolationException("Error saving department"));

        APIRequestException exception = Assertions.assertThrows(APIRequestException.class, () -> {
            departmentService.createDepartment(departmentDTO);
        });

        Assertions.assertTrue(exception.getMessage().contains("Exception occurred on attempt to create an employee"));
    }

    @Test
    public void updateDepartment_ShouldUpdateDepartment_UpdatedDepartment(){
        when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(department);
        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(department));

        DepartmentDTO newDepartment = DepartmentDTO.builder()
                .name("New Department")
                .cost_center_code("NWD202")
                .employees(List.of(employeeDTO))
                .build();

        DepartmentDTO updatedDepartment = departmentService.updateDepartment(newDepartment, 1);

        Assertions.assertNotNull(updatedDepartment);
        Assertions.assertEquals(newDepartment.getName(), updatedDepartment.getName());
        Assertions.assertEquals(newDepartment.getCost_center_code(), updatedDepartment.getCost_center_code());
    }
    @Test
    public void updateDepartment_ShouldThrowDBException_WhenDepartmentSaveFails(){
        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(department));
        when(departmentRepository.save(Mockito.any(Department.class))).thenThrow(new DataIntegrityViolationException("Error saving department"));

        APIRequestException exception = Assertions.assertThrows(APIRequestException.class, () -> {
            departmentService.updateDepartment(DepartmentDTO.builder().build(), 1);
        });

        Assertions.assertTrue(exception.getMessage().contains("Exception occurred on attempt to save department with id"));
    }

    @Test
    public void updateDepartment_ShouldThrowIllegalArgException_WhenDepartmentSaveFails(){
        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(department));
        when(departmentRepository.save(Mockito.any(Department.class))).thenThrow(new IllegalArgumentException("Wrong arguments"));

        APIRequestException exception = Assertions.assertThrows(APIRequestException.class, () -> {
            departmentService.updateDepartment(departmentDTO, 1);
        });

        Assertions.assertTrue(exception.getMessage().contains("Wrong arguments were passed to update department with id"));
    }
    @Test
    public void deleteDepartmentById_ShouldDeleteDepartment_Nothing(){
        doNothing().when(departmentRepository).deleteById(Mockito.anyInt());

        departmentService.deleteDepartmentById(1);

        verify(departmentRepository, times(1)).deleteById(1);
    }

    @Test
    public void addEmployee_ShouldAddEmployeeToDepartment_UpdatedDepartment(){
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(employee));
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);

        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(department));
        when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(department);

        DepartmentDTO updatedDepartment = departmentService.addEmployee(List.of(employeeDTO), 1);

        verify(employeeRepository, times(1)).findById(Mockito.anyInt());
        verify(employeeRepository, times(1)).save(Mockito.any(Employee.class));
        verify(departmentRepository, times(1)).findById(Mockito.anyInt());
        verify(departmentRepository, times(1)).save(Mockito.any(Department.class));

        Assertions.assertNotNull(updatedDepartment);
        Assertions.assertEquals(departmentDTO.getName(),updatedDepartment.getName());
        Assertions.assertFalse(updatedDepartment.getEmployees().isEmpty());
    }

    @Test
    public void addEmployee_ShouldThrowException_WhenEmployeeNotFound() {
        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(new Department()));
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(APIRequestException.class, () -> {
            departmentService.addEmployee(List.of(employeeDTO), 1);
        });

        verify(departmentRepository, times(1)).findById(Mockito.anyInt());
        verify(employeeRepository, times(1)).findById(Mockito.anyInt());
    }

    @Test
    public void addEmployee_ShouldThrowException_WhenEmployeeSaveFails() {
        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(department));
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));

        when(employeeRepository.save(any(Employee.class))).thenThrow(new DataIntegrityViolationException("Error saving employee"));

        APIRequestException exception = Assertions.assertThrows(APIRequestException.class, () -> {
            departmentService.addEmployee(List.of(employeeDTO), 1);
        });

        Assertions.assertTrue(exception.getMessage().contains("Exception occurred on attempt to save employee with id:"));
    }

    @Test
    public void addEmployee_ShouldThrowException_WhenDepartmentSaveFails() {
        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(department));
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        when(departmentRepository.save(any(Department.class))).thenThrow(new DataIntegrityViolationException("Error saving department"));

        APIRequestException exception = Assertions.assertThrows(APIRequestException.class, () -> {
            departmentService.addEmployee(List.of(employeeDTO), 1);
        });

        Assertions.assertTrue(exception.getMessage().contains("Exception occurred on attempt to save department with id:"));
    }

    @Test
    public void removeEmployee_ShouldRemoveEmployeeFromDepartment_UpdatedDepartment(){
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(employee));
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);

        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(department));
        when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(department);

        DepartmentDTO updatedDepartment = departmentService.addEmployee(List.of(employeeDTO), 1);
        Assertions.assertFalse(updatedDepartment.getEmployees().isEmpty());

        updatedDepartment = departmentService.removeEmployee(List.of(employeeDTO), 1);

        verify(departmentRepository, times(2)).findById(Mockito.anyInt());
        verify(departmentRepository, times(2)).save(Mockito.any(Department.class));
        verify(employeeRepository, times(2)).findById(Mockito.anyInt());
        verify(employeeRepository, times(2)).save(Mockito.any(Employee.class));

        Assertions.assertNotNull(updatedDepartment);
        Assertions.assertEquals(department.getName(),updatedDepartment.getName());
        Assertions.assertTrue(updatedDepartment.getEmployees().isEmpty());
    }

    @Test void removeEmployee_ShouldReturnNull_Null(){
        DepartmentDTO updatedDepartment = departmentService.removeEmployee(List.of(), 1);

        Assertions.assertNull(updatedDepartment);
    }

    @Test
    public void removeEmployee_ShouldThrowException_WhenEmployeeNotFound() {
        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(new Department()));
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(APIRequestException.class, () -> {
            departmentService.removeEmployee(List.of(employeeDTO), 1);
        });

        verify(departmentRepository, times(1)).findById(Mockito.anyInt());
        verify(employeeRepository, times(1)).findById(Mockito.anyInt());
    }

    @Test
    public void removeEmployee_ShouldThrowException_WhenEmployeeSaveFails() {
        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(department));
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));

        when(employeeRepository.save(any(Employee.class))).thenThrow(new DataIntegrityViolationException("Error"));

        APIRequestException exception = Assertions.assertThrows(APIRequestException.class, () -> {
            departmentService.removeEmployee(List.of(employeeDTO), 1);
        });

        Assertions.assertTrue(exception.getMessage().contains("Exception occurred on attempt to save employee with id:"));
    }

    @Test
    public void removeEmployee_ShouldThrowException_WhenDepartmentSaveFails() {
        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(department));
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        when(departmentRepository.save(any(Department.class))).thenThrow(new DataIntegrityViolationException("Error saving department"));

        APIRequestException exception = Assertions.assertThrows(APIRequestException.class, () -> {
            departmentService.removeEmployee(List.of(employeeDTO), 1);
        });

        Assertions.assertTrue(exception.getMessage().contains("Exception occurred on attempt to save department with id:"));
    }
}
