package com.example.CATSEmployee.mapper;

import com.example.CATSEmployee.DTO.concrete.DepartmentDTO;
import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.exception.APIRequestException;
import com.example.CATSEmployee.models.concrete.Department;
import com.example.CATSEmployee.models.concrete.Employee;
import com.example.CATSEmployee.service.implementations.EmployeeServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.stream.Collectors;

@Component
public class DepartmentMapper {

    private static EmployeeServiceImpl employeeService;

    public DepartmentMapper(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    public static DepartmentDTO toDto(Department department) {
        if(department == null) return null;

        return DepartmentDTO.builder()
                .cost_center_code(department.getCost_center_code())
                .tribe_code(department.getTribe_code())
                .employees(department.getEmployees().stream()
                        .map(DepartmentMapper::EmployeeToDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static Department toEntity(DepartmentDTO departmentDTO) {
        if (departmentDTO == null) return null;

        return Department.builder()
                .cost_center_code(departmentDTO.getCost_center_code())
                .tribe_code(departmentDTO.getTribe_code())
                .employees(departmentDTO.getEmployees().stream()
                        .map(DepartmentMapper::EmployeeToEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    public static EmployeeDTO EmployeeToDto(Employee employee) {
        if (employee == null) return null;

        return EmployeeDTO.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .operational_head(employee.getOperational_head())
                .direct_supervisor(DepartmentMapper.DirectSupervisorToDto(employee.getDirect_supervisor()))
                .subordinate(DepartmentMapper.DirectSupervisorToDto(employee.getSubordinate()))
                .build();
    }
    public static Employee EmployeeToEntity(EmployeeDTO employeeDTO) {
        if(employeeDTO == null) return null;

        return Employee.builder()
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .email(employeeDTO.getEmail())
                .operational_head(employeeDTO.getOperational_head())
                .direct_supervisor(DepartmentMapper.DirectSupervisorToEntity(employeeDTO.getDirect_supervisor()))
                .subordinate(DepartmentMapper.DirectSupervisorToEntity(employeeDTO.getSubordinate()))
                .build();
    }

    public static Employee DirectSupervisorToEntity(EmployeeDTO employeeDTO) {
        if(employeeDTO == null) return null;

        return Employee.builder()
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .email(employeeDTO.getEmail())
                .operational_head(employeeDTO.getOperational_head())
                .build();
    }

    public static EmployeeDTO DirectSupervisorToDto(Employee employee) {
        if(employee == null) return null;

        return EmployeeDTO.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .operational_head(employee.getOperational_head())
                .build();
    }
}
