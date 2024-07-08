package com.example.CATSEmployee.mapper;

import com.example.CATSEmployee.DTO.concrete.DepartmentDTO;
import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.models.concrete.Department;
import com.example.CATSEmployee.models.concrete.Employee;
import com.example.CATSEmployee.service.implementations.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    private static EmployeeServiceImpl employeeService;

    @Autowired
    public EmployeeMapper(EmployeeServiceImpl employeeService) {
        EmployeeMapper.employeeService = employeeService;
    }

    public static EmployeeDTO toDto(Employee employee) {
        if (employee == null) return null;

        return EmployeeDTO.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .operational_head(employee.getOperational_head())
                .direct_supervisor_id(employee.getDirect_supervisor() != null ? employee.getDirect_supervisor().getId() : null)
                .subordinate_id(employee.getSubordinate() != null ? employee.getSubordinate().getId() : null)
                .department(DepartmentToDto(employee.getDepartment()))
                .build();
    }

    public static Employee toEntity(EmployeeDTO employeeDTO) {
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
                .department(DepartmentToEntity(employeeDTO.getDepartment()))
                .build();
    }

    private static Department DepartmentToEntity(DepartmentDTO departmentDTO) {
        if (departmentDTO == null) return null;

        return Department.builder()
                .cost_center_code(departmentDTO.getCost_center_code())
                .tribe_code(departmentDTO.getTribe_code())
                .build();
    }

    private static DepartmentDTO DepartmentToDto(Department department) {
        if (department == null) return null;

        return DepartmentDTO.builder()
                .cost_center_code(department.getCost_center_code())
                .tribe_code(department.getTribe_code())
                .build();
    }
}
