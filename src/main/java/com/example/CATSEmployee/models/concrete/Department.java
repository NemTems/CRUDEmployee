package com.example.CATSEmployee.models.concrete;

import com.example.CATSEmployee.models.common.BaseClass;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Department extends BaseClass {
    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String cost_center_code;

    @OneToMany(mappedBy = "department", cascade = CascadeType.DETACH)
    @JsonManagedReference
    private List<Employee> employees;

}
