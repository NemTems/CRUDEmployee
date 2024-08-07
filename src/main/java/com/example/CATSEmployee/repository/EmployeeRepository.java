package com.example.CATSEmployee.repository;

import com.example.CATSEmployee.models.concrete.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("SELECT e " +
            "FROM Employee e " +
            "WHERE " +
            "(:managerId IS NULL OR (e.department.id = (SELECT emp.department.id FROM Employee emp WHERE emp.id = :managerId AND (emp.operational_head = TRUE OR emp.direct_supervisor.id IS NULL)))) AND " +
            "(:leadId IS NULL OR (e.operational_head = FALSE AND e.direct_supervisor.id = :leadId)) AND " +
            "(:departmentId IS NULL OR e.department.id = :departmentId)")
    Page<Employee> findBySpecificParameters(
            @Param("departmentId") Integer departmentId,
            @Param("managerId") Integer managerId,
            @Param("leadId") Integer leadId,
            Pageable pageable
    );
}
