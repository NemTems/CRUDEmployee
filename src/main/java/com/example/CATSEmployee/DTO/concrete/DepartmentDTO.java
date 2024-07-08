package com.example.CATSEmployee.DTO.concrete;

import com.example.CATSEmployee.models.common.BaseClass;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DepartmentDTO {
    private String tribe_code;
    private String cost_center_code;
    private List<EmployeeDTO> employees = new ArrayList<>();
}
