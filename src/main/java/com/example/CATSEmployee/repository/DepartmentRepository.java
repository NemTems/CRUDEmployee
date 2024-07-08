package com.example.CATSEmployee.repository;

import com.example.CATSEmployee.models.concrete.Department;
import com.example.CATSEmployee.models.concrete.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
