package com.example.CATSEmployee.service.interfaces;

import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.models.concrete.Employee;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO getEmployeeById(int id);

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(EmployeeDTO employeeDTO, int id);

    void deleteEmployeeById(int id);
}
