package com.example.CATSEmployee.DTO.concrete;

import com.example.CATSEmployee.models.common.BaseClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class DepartmentDTO extends BaseClass {
    private String name;
    private String cost_center_code;
    private List<EmployeeDTO> employees = new ArrayList<>();

    public void addEmployee(EmployeeDTO employeeDTO) {
        employeeDTO.setDepartment_id(this.getId());
        employees.add(employeeDTO);
    }

    public void removeEmployee(EmployeeDTO employeeDTO) {
        employeeDTO.setDepartment_id(0);
        employees.remove(employeeDTO);
    }

    public boolean hasEmployee(EmployeeDTO employeeDTO) {
        Optional<EmployeeDTO> existingSubordinate = employees.stream()
                .filter(emp -> emp.getId() == employeeDTO.getId() && emp.getFirstName() != null)
                .findFirst();

        return existingSubordinate.isPresent();
    }
}
