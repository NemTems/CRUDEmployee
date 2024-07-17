package com.example.CATSEmployee.DTO.concrete;

import com.example.CATSEmployee.models.common.BaseClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class DepartmentDTO extends BaseClass {
    private String name;
    private String cost_center_code;
    private List<EmployeeDTO> employees;
}
