package com.example.CATSEmployee.mapper;

import com.example.CATSEmployee.DTO.concrete.DepartmentDTO;
import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.models.concrete.Department;
import com.example.CATSEmployee.models.concrete.Employee;
import com.example.CATSEmployee.service.implementations.EmployeeServiceImpl;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class DepartmentMapper {

    private static EmployeeServiceImpl employeeService;

    public static DepartmentDTO toDto(Department department) {
        if (department == null) return null;

        return DepartmentDTO.builder()
                .id(department.getId())
                .cost_center_code(department.getCost_center_code())
                .name(department.getName())
                .employees(!CollectionUtils.isEmpty(department.getEmployees())
                        ? department.getEmployees().stream()
                                .map(DepartmentMapper::EmployeeToDto)
                                .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }

    public static Department toEntity(DepartmentDTO departmentDTO) {
        if (departmentDTO == null) return null;

        return Department.builder()
                .id(departmentDTO.getId())
                .cost_center_code(departmentDTO.getCost_center_code())
                .name(departmentDTO.getName())
                .employees(!CollectionUtils.isEmpty(departmentDTO.getEmployees())
                        ? departmentDTO.getEmployees().stream()
                                .map(DepartmentMapper::EmployeeToEntity)
                                .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }

    private static EmployeeDTO EmployeeToDto(Employee employee) {
        if (employee == null) return null;

        return EmployeeDTO.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .operational_head(employee.getOperational_head())
                .direct_supervisor_id(!Objects.isNull(employee.getDirect_supervisor())
                        ? employee.getDirect_supervisor().getId()
                        : 0)
                .build();
    }

    private static Employee EmployeeToEntity(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) return null;

        return Employee.builder()
                .id(employeeDTO.getId())
                .build();
    }
}
