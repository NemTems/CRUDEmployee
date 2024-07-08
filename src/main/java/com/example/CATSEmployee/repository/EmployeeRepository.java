package com.example.CATSEmployee.repository;

import com.example.CATSEmployee.models.concrete.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
