package com.example.CATSEmployee.repository;

import com.example.CATSEmployee.models.concrete.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
