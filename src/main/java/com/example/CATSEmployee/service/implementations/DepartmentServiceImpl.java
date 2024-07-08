package com.example.CATSEmployee.service.implementations;

import com.example.CATSEmployee.DTO.concrete.DepartmentDTO;
import com.example.CATSEmployee.exception.APIRequestException;
import com.example.CATSEmployee.mapper.DepartmentMapper;
import com.example.CATSEmployee.repository.DepartmentRepository;
import com.example.CATSEmployee.service.interfaces.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(DepartmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO getDepartmentById(int id) {
        return DepartmentMapper.toDto(departmentRepository.findById(id)
                .orElseThrow(() -> new APIRequestException("Department not found")));
    }

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        return DepartmentMapper.toDto(departmentRepository.save(DepartmentMapper.toEntity(departmentDTO)));
    }

    @Override
    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO, int id) {
        try {
            DepartmentDTO updateDepartmentDTO = DepartmentMapper.toDto(departmentRepository.findById(id)
                    .orElseThrow(() -> new APIRequestException("Department not found")));
            updateDepartmentDTO.setCost_center_code(departmentDTO.getCost_center_code());
            updateDepartmentDTO.setTribe_code(departmentDTO.getTribe_code());
            updateDepartmentDTO.setEmployees(departmentDTO.getEmployees());

            departmentRepository.save(DepartmentMapper.toEntity(updateDepartmentDTO));
            return updateDepartmentDTO;
        }
        catch (IllegalArgumentException e) {
            throw new APIRequestException(e.getMessage(), e);
        }
        catch (ResponseStatusException e) {
            throw new APIRequestException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteDepartmentById(int id) {
        departmentRepository.deleteById(id);
    }
}
