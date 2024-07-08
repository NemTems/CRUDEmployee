package com.example.CATSEmployee.DTO.concrete;

import com.example.CATSEmployee.models.concrete.Department;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmployeeDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Boolean operational_head;
    private int direct_supervisor_id;
    private int subordinate_id;
    private DepartmentDTO department;
}
