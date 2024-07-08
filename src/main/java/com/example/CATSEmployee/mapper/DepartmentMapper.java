package com.example.CATSEmployee.mapper;

import com.example.CATSEmployee.DTO.concrete.DepartmentDTO;
import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.models.concrete.Department;
import com.example.CATSEmployee.models.concrete.Employee;
import com.example.CATSEmployee.service.implementations.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class DepartmentMapper {

    private static EmployeeServiceImpl employeeService;

    @Autowired
    public DepartmentMapper(EmployeeServiceImpl employeeService) {
        DepartmentMapper.employeeService = employeeService;
    }

    public static DepartmentDTO toDto(Department department) {
        if (department == null) return null;

        return DepartmentDTO.builder()
                .cost_center_code(department.getCost_center_code())
                .tribe_code(department.getTribe_code())
                .employees(department.getEmployees() != null ?
                        department.getEmployees().stream()
                                .map(DepartmentMapper::EmployeeToDto)
                                .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }

    public static Department toEntity(DepartmentDTO departmentDTO) {
        if (departmentDTO == null) return null;

        return Department.builder()
                .cost_center_code(departmentDTO.getCost_center_code())
                .tribe_code(departmentDTO.getTribe_code())
                .employees(departmentDTO.getEmployees() != null ?
                        departmentDTO.getEmployees().stream()
                                .map(DepartmentMapper::EmployeeToEntity)
                                .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }

    public static EmployeeDTO EmployeeToDto(Employee employee) {
        if (employee == null) return null;

        return EmployeeDTO.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .operational_head(employee.getOperational_head())
                .direct_supervisor_id(employee.getDirect_supervisor() != null ?
                        employee.getDirect_supervisor().getId() : null)
                .subordinate_id(employee.getSubordinate() != null ?
                        employee.getSubordinate().getId() : null)
                .build();
    }

    public static Employee EmployeeToEntity(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) return null;

        Employee supervisor = (employeeDTO.getDirect_supervisor_id() > 0) ?
                DepartmentMapper.DirectSupervisorToEntity(employeeService.getEmployeeById(employeeDTO.getDirect_supervisor_id())) : null;
        Employee subordinate = (employeeDTO.getSubordinate_id() > 0) ?
                DepartmentMapper.DirectSupervisorToEntity(employeeService.getEmployeeById(employeeDTO.getSubordinate_id())) : null;

        return Employee.builder()
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .email(employeeDTO.getEmail())
                .operational_head(employeeDTO.getOperational_head())
                .direct_supervisor(supervisor)
                .subordinate(subordinate)
                .build();
    }

    public static Employee DirectSupervisorToEntity(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) return null;

        return Employee.builder()
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .email(employeeDTO.getEmail())
                .operational_head(employeeDTO.getOperational_head())
                .build();
    }
}
