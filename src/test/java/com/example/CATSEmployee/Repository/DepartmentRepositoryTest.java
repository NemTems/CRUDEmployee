package com.example.CATSEmployee.Repository;

import com.example.CATSEmployee.models.concrete.Department;
import com.example.CATSEmployee.repository.DepartmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DepartmentRepositoryTest {
    @Autowired
    private DepartmentRepository departmentRepository;

    private Department departmentOne, departmentTwo;

    @BeforeEach
    public void setUp(){
        departmentOne = Department.builder()
                .name("Finance")
                .cost_center_code("FIN001")
                .employees(new ArrayList<>())
                .build();

        departmentTwo = Department.builder()
                .name("Human Resources")
                .cost_center_code("HR001")
                .employees(new ArrayList<>())
                .build();
    }

    @Test
    public void DepartmentRepository_SaveDepartmentTest_Saved(){
        Department department = departmentRepository.save(departmentOne);

        Assertions.assertNotNull(department);
        Assertions.assertTrue(department.getId() > 0);
    }

    @Test
    public void DepartmentRepository_FindAllDepartmentsTest_AllDepartment(){
        departmentRepository.save(departmentOne);
        departmentRepository.save(departmentTwo);

        List<Department> departmentList = departmentRepository.findAll();

        Assertions.assertNotNull(departmentList);
        Assertions.assertEquals(departmentOne, departmentList.getFirst());
        Assertions.assertEquals(departmentTwo, departmentList.getLast());
    }

    @Test
    public void DepartmentRepository_FindDepartmentByIdTest_OneDepartment(){
        departmentRepository.save(departmentOne);
        Department savedDepartment = departmentRepository.save(departmentTwo);

        Department foundDepartment = departmentRepository.findById(savedDepartment.getId()).get();

        Assertions.assertNotNull(foundDepartment);
        Assertions.assertEquals(savedDepartment.getId(), foundDepartment.getId());
        Assertions.assertEquals(savedDepartment.getName(), foundDepartment.getName());
    }

    @Test
    public void DepartmentRepository_DeleteDepartmentByIdTest_Nothing(){
        departmentRepository.save(departmentOne);
        Department savedDepartment = departmentRepository.save(departmentTwo);

        Assertions.assertTrue(departmentRepository.findById(savedDepartment.getId()).isPresent());

        departmentRepository.deleteById(savedDepartment.getId());

        Assertions.assertFalse(departmentRepository.findById(savedDepartment.getId()).isPresent());
    }
}
