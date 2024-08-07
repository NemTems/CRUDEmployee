package com.example.CATSEmployee.controller;

import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.service.implementations.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeServiceImpl employeeService;

    @Autowired
    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/showAll")
    public ResponseEntity<List<EmployeeDTO>> showAllEmployees(
            @RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
            @RequestParam(value = "limit", defaultValue = "10", required = false) int limit,
            @RequestParam(value = "department_id", required = false) Integer departmentId,
            @RequestParam(value = "manager_id", required = false) Integer managerId,
            @RequestParam(value = "lead_id", required = false) Integer leadId
    ) {

        return new ResponseEntity<>(employeeService.getAllEmployees(offset, limit, departmentId, managerId, leadId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> showEmployee(@PathVariable int id) {
        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return new ResponseEntity<>(employeeService.createEmployee(employeeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable int id) {
        return new ResponseEntity<>(employeeService.updateEmployee(employeeDTO, id), HttpStatus.OK);
    }

    @PutMapping("/{id}/add/subordinates")
    public ResponseEntity<EmployeeDTO> addSubordinatesToEmployee(@RequestBody List<EmployeeDTO> subordinates, @PathVariable int id) {
        return new ResponseEntity<>(employeeService.addSubordinates(subordinates, id), HttpStatus.OK);
    }

    @PutMapping("/{id}/remove/subordinates")
    public ResponseEntity<EmployeeDTO> removeSubordinateFromEmployee(@RequestBody List<EmployeeDTO> subordinates, @PathVariable int id) {
        return new ResponseEntity<>(employeeService.removeSubordinates(subordinates, id), HttpStatus.OK);
    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployeeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
