package com.example.CATSEmployee.controller;

import com.example.CATSEmployee.DTO.concrete.DepartmentDTO;
import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.service.implementations.DepartmentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {
    private final DepartmentServiceImpl departmentService;

    public DepartmentController(DepartmentServiceImpl departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/showAll")
    private ResponseEntity<List<DepartmentDTO>> showAllDepartments() {
        return new ResponseEntity<>(departmentService.getAllDepartments(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<DepartmentDTO> showEmployeeDepartment(@PathVariable int id) {
        return new ResponseEntity<>(departmentService.getDepartmentById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    private ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        return new ResponseEntity<>(departmentService.createDepartment(departmentDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    private ResponseEntity<DepartmentDTO> updateDepartment(@RequestBody DepartmentDTO departmentDTO, @PathVariable int id) {
        return new ResponseEntity<>(departmentService.updateDepartment(departmentDTO, id), HttpStatus.OK);
    }

    @PutMapping("/{id}/add/employees")
    public ResponseEntity<DepartmentDTO> addEmployeesToDepartment(@RequestBody List<EmployeeDTO> employeeDTOList, @PathVariable int id) {
        return new ResponseEntity<>(departmentService.addEmployee(employeeDTOList, id), HttpStatus.OK);
    }

    @PutMapping("/{id}/remove/employees")
    public ResponseEntity<DepartmentDTO> removeEmployeesFromDepartment(@RequestBody List<EmployeeDTO> employeeDTOList, @PathVariable int id) {
        return new ResponseEntity<>(departmentService.removeEmployee(employeeDTOList, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    private ResponseEntity<Void> deleteDepartment(@PathVariable int id) {
        departmentService.deleteDepartmentById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
