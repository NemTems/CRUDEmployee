package com.example.CATSEmployee.service.interfaces;

import com.example.CATSEmployee.DTO.concrete.DepartmentDTO;

import java.util.List;

public interface DepartmentService {
    List<DepartmentDTO> getAllDepartments();

    DepartmentDTO getDepartmentById(int id);

    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

    DepartmentDTO updateDepartment(DepartmentDTO departmentDTO, int id);

    void deleteDepartmentById(int id);
}
