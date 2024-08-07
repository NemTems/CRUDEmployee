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
public class EmployeeDTO extends BaseClass {
    private String firstName;
    private String lastName;
    private String email;
    private Boolean operational_head = true;
    private int direct_supervisor_id;
    private List<EmployeeDTO> subordinates = new ArrayList<>();
    private int department_id;

    public void addSubordinate(EmployeeDTO subordinate) {
        subordinate.setOperational_head(false);
        subordinate.setDirect_supervisor_id(this.getId());
        subordinates.add(subordinate);
    }

    public void removeSubordinate(EmployeeDTO subordinate) {
        subordinate.setOperational_head(true);
        subordinate.setDirect_supervisor_id(0);
        subordinates.remove(subordinate);
    }

    public boolean hasSubordinate(EmployeeDTO subordinate) {
        Optional<EmployeeDTO> existingSubordinate = subordinates.stream()
                .filter(sub -> sub.getId() == subordinate.getId() && sub.getFirstName() != null)
                .findFirst();

        return existingSubordinate.isPresent();
    }
}
