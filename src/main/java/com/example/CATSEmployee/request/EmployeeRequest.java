package com.example.CATSEmployee.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String department_cost_center_code;
    private String direct_supervisor;
    private Boolean operational_head;
    private String tribe_code;
}
