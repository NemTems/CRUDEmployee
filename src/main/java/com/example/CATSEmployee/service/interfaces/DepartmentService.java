package com.example.CATSEmployee.service.interfaces;

import com.example.CATSEmployee.DTO.concrete.DepartmentDTO;
import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;

import java.util.List;

public interface DepartmentService {
    List<DepartmentDTO> getAllDepartments();

    DepartmentDTO getDepartmentById(int id);

    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

    DepartmentDTO addEmployee(List<EmployeeDTO> employeeDTOList, int id);

    DepartmentDTO removeEmployee(List<EmployeeDTO> employeeDTOList, int id);

    DepartmentDTO updateDepartment(DepartmentDTO departmentDTO, int id);

    void deleteDepartmentById(int id);
}
