package com.example.CATSEmployee.DTO.common;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Data
public class BaseClassDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
