package com.example.CATSEmployee.repository;

import com.example.CATSEmployee.models.concrete.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("SELECT e FROM Employee e WHERE " +
            "(:leadId IS NULL OR (e.operational_head = TRUE AND e.id = :leadId AND e.direct_supervisor IS NULL)) AND " +
            "(:managerId IS NULL OR (e.operational_head = FALSE AND e.id = :managerId AND e.direct_supervisor IS NOT NULL)) AND " +
            "(:departmentId IS NULL OR e.department.id = :departmentId)")
    Page<Employee> findBySpecificParameters(
            @Param("departmentId") Integer departmentId,
            @Param("managerId") Integer managerId,
            @Param("leadId") Integer leadId,
            Pageable pageable
    );
}
