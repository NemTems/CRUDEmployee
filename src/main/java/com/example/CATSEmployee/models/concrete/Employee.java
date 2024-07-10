package com.example.CATSEmployee.models.concrete;

import com.example.CATSEmployee.models.common.BaseClass;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Employee extends BaseClass {
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    private String email;

    private Boolean operational_head = false;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "subordinate")
    @JsonManagedReference
    private Employee direct_supervisor;

    @OneToOne(mappedBy = "direct_supervisor", cascade = CascadeType.ALL)
    @JsonBackReference
    private Employee subordinate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "department_employee")
    @JsonBackReference
    private Department department;
}
