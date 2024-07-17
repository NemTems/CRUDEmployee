package com.example.CATSEmployee.models.concrete;

import com.example.CATSEmployee.models.common.BaseClass;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends BaseClass {
    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private Boolean operational_head = false;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, mappedBy = "direct_supervisor")
    @JsonBackReference
    private List<Employee> subordinates;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "direct_supervisor_id")
    @JsonManagedReference
    private Employee direct_supervisor;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "department_employee_id")
    @JsonBackReference
    private Department department;

}
