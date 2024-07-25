package com.example.CATSEmployee.Service;

import com.example.CATSEmployee.DTO.concrete.DepartmentDTO;
import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
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

import java.util.ArrayList;
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

    private DepartmentDTO departmentOneDTO;
    private DepartmentDTO departmentTwoDTO;

    private Department departmentOne;
    private Department departmentTwo;

    private EmployeeDTO employeeDTO;

    private Employee employee;

    @BeforeEach
    public void setUp(){
        departmentOneDTO = DepartmentDTO.builder()
                .name("Finance")
                .cost_center_code("FIN001")
                .build();

        departmentTwoDTO = DepartmentDTO.builder()
                .name("Human Resources")
                .cost_center_code("HR001")
                .build();

        departmentOne = Department.builder()
                .name("Finance")
                .cost_center_code("FIN001")
                .build();

        departmentTwo = Department.builder()
                .name("Human Resources")
                .cost_center_code("HR001")
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
    public void DepartmentService_CreateDepartmentTest_Created(){
        when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(departmentOne);

        DepartmentDTO savedDepartment = departmentService.createDepartment(departmentOneDTO);

        Assertions.assertNotNull(savedDepartment);
        Assertions.assertEquals(departmentOneDTO.getName(), savedDepartment.getName());
        Assertions.assertEquals(departmentOneDTO.getCost_center_code(), savedDepartment.getCost_center_code());
        Assertions.assertEquals(new ArrayList<>(), savedDepartment.getEmployees());
    }

    @Test
    public void DepartmentService_GetAllDepartmentsTest_AllDepartments(){
        List<Department> departmentList = List.of(departmentOne,departmentTwo);

        when(departmentRepository.findAll()).thenReturn(departmentList);

        List<DepartmentDTO> departmentDTOList = departmentService.getAllDepartments();

        Assertions.assertNotNull(departmentDTOList);
        Assertions.assertEquals(departmentOneDTO.getId(), departmentDTOList.getFirst().getId());
        Assertions.assertEquals(departmentOneDTO.getName(), departmentDTOList.getFirst().getName());
        Assertions.assertEquals(departmentTwoDTO.getId(), departmentDTOList.getLast().getId());
        Assertions.assertEquals(departmentTwoDTO.getName(), departmentDTOList.getLast().getName());
    }

    @Test
    public void DepartmentService_GetDepartmentById_OneDepartment(){
        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(departmentOne));

        DepartmentDTO foundDepartment = departmentService.getDepartmentById(1);

        Assertions.assertNotNull(foundDepartment);
        Assertions.assertEquals(departmentOne.getName(), foundDepartment.getName());
    }

    @Test
    public void DepartmentService_UpdateDepartmentById_UpdatedDepartment(){
        when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(departmentOne);
        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(departmentOne));

        DepartmentDTO newDepartment = DepartmentDTO.builder()
                .name("New Department")
                .build();

        DepartmentDTO updatedDepartment = departmentService.updateDepartment(newDepartment, 1);

        Assertions.assertNotNull(updatedDepartment);
        Assertions.assertEquals(newDepartment.getName(), updatedDepartment.getName());
        Assertions.assertEquals(departmentOne.getCost_center_code(), updatedDepartment.getCost_center_code());
    }

    @Test
    public void DepartmentService_DeleteDepartmentById_Nothing(){
        doNothing().when(departmentRepository).deleteById(Mockito.anyInt());

        departmentService.deleteDepartmentById(1);

        verify(departmentRepository, times(1)).deleteById(1);
    }

    @Test
    public void DepartmentService_AddEmployeeToDepartmentById_UpdatedDepartment(){
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(employee));
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);

        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(departmentOne));
        when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(departmentOne);

        DepartmentDTO updatedDepartment = departmentService.addEmployee(List.of(employeeDTO), 1);

        Assertions.assertNotNull(updatedDepartment);
        Assertions.assertEquals(departmentOneDTO.getName(),updatedDepartment.getName());
        Assertions.assertFalse(updatedDepartment.getEmployees().isEmpty());
    }
    @Test
    public void DepartmentService_RemoveEmployeeToDepartmentById_UpdatedDepartment(){
        when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(employee));
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);

        when(departmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(departmentTwo));
        when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(departmentTwo);

        DepartmentDTO updatedDepartment = departmentService.addEmployee(List.of(employeeDTO), 1);
        Assertions.assertFalse(updatedDepartment.getEmployees().isEmpty());

        updatedDepartment = departmentService.removeEmployee(List.of(employeeDTO), 1);

        verify(departmentRepository, times(2)).findById(Mockito.anyInt());
        verify(departmentRepository, times(2)).save(Mockito.any(Department.class));
        verify(employeeRepository, times(2)).findById(Mockito.anyInt());
        verify(employeeRepository, times(2)).save(Mockito.any(Employee.class));

        Assertions.assertNotNull(updatedDepartment);
        Assertions.assertEquals(departmentTwo.getName(),updatedDepartment.getName());
        Assertions.assertTrue(updatedDepartment.getEmployees().isEmpty());
    }
}
