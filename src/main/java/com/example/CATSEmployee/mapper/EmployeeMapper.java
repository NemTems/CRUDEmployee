package com.example.CATSEmployee.mapper;

import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.models.concrete.Department;
import com.example.CATSEmployee.models.concrete.Employee;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EmployeeMapper {

    public static EmployeeDTO toDto(Employee employee) {
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
                .subordinates(!CollectionUtils.isEmpty(employee.getSubordinates())
                        ? employee.getSubordinates().stream()
                        .map(EmployeeMapper::BasicEmployeeToDto)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .department_id(!Objects.isNull(employee.getDepartment())
                        ? employee.getDepartment().getId()
                        : 0)
                .build();
    }

    public static Employee toEntity(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) return null;

        Employee employee = Employee.builder()
                .id(employeeDTO.getId())
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .email(employeeDTO.getEmail())
                .operational_head(employeeDTO.getOperational_head())
                .build();

        employee.setDepartment(employeeDTO.getDepartment_id() != 0
                ? Department.builder()
                .id(employeeDTO.getDepartment_id())
                .build()
                : null
        );

        employee.setDirect_supervisor(employeeDTO.getDirect_supervisor_id() != 0
                ? Employee.builder()
                    .id(employeeDTO.getDirect_supervisor_id())
                    .build()
                : null
        );

        employee.setSubordinates(!CollectionUtils.isEmpty(employeeDTO.getSubordinates())
                ? employeeDTO.getSubordinates().stream()
                    .map(EmployeeMapper::BasicEmployeeToEntity)
                    .collect(Collectors.toList())
                    : new ArrayList<>()
        );

        return employee;
    }

    public static EmployeeDTO BasicEmployeeToDto(Employee employee) {
        if (employee == null) return null;

        return EmployeeDTO.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .operational_head(employee.getOperational_head())
                .department_id(!Objects.isNull(employee.getDepartment())
                        ? employee.getDepartment().getId()
                        : 0)
                .build();
    }

    public static Employee BasicEmployeeToEntity(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) return null;

        Employee employee = Employee.builder()
                .id(employeeDTO.getId())
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .email(employeeDTO.getEmail())
                .operational_head(employeeDTO.getOperational_head())
                .build();

        employee.setDepartment(employeeDTO.getDepartment_id() != 0
                ? Department.builder()
                .id(employeeDTO.getDepartment_id())
                .build()
                : null
        );

        return employee;
    }
}


