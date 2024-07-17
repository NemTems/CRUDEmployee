package com.example.CATSEmployee.DTO.concrete;

import com.example.CATSEmployee.models.common.BaseClass;
import com.example.CATSEmployee.models.concrete.Employee;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class EmployeeDTO extends BaseClass {
    private String firstName;
    private String lastName;
    private String email;
    private Boolean operational_head;
    private int direct_supervisor_id;
    private List<EmployeeDTO> subordinates;
    private int department_id;
}
