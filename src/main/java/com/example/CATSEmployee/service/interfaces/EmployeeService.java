package com.example.CATSEmployee.service.interfaces;

import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO getEmployeeById(int id);

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO addSubordinates(List<EmployeeDTO> subordinates, int id);

    EmployeeDTO removeSubordinates(List<EmployeeDTO> subordinates, int id);

    EmployeeDTO updateEmployee(EmployeeDTO employeeDTO, int id);

    void deleteEmployeeById(int id);
}
