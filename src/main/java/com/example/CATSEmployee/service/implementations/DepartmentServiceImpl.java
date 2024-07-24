package com.example.CATSEmployee.service.implementations;

import com.example.CATSEmployee.DTO.concrete.DepartmentDTO;
import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.exception.APIRequestException;
import com.example.CATSEmployee.mapper.DepartmentMapper;
import com.example.CATSEmployee.mapper.EmployeeMapper;
import com.example.CATSEmployee.repository.DepartmentRepository;
import com.example.CATSEmployee.repository.EmployeeRepository;
import com.example.CATSEmployee.service.interfaces.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
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

    @Transactional
    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        try {
            DepartmentDTO savedDepartment = DepartmentMapper.toDto(departmentRepository.save(DepartmentMapper.toEntity(departmentDTO)));
            addEmployee(departmentDTO.getEmployees(), savedDepartment.getId());

            return DepartmentMapper.toDto(departmentRepository.save(DepartmentMapper.toEntity(savedDepartment)));
        }
        catch (DataIntegrityViolationException e) {
            throw new APIRequestException("Exception occurred on attempt to create an employee" + "\nError message: " + e.getMessage(),e);
        }
    }

    @Transactional
    @Override
    public DepartmentDTO addEmployee(List<EmployeeDTO> employeeDTOList, int id) {
        try {
            if (employeeDTOList.isEmpty()) return null;

            DepartmentDTO departmentDTO = getDepartmentById(id);

            for (EmployeeDTO employeeDTO : employeeDTOList) {
                EmployeeDTO savedEmployee = EmployeeMapper.toDto(employeeRepository.findById(employeeDTO.getId())
                        .orElseThrow(() -> new APIRequestException("Employee not found")));
                try {
                    if(!departmentDTO.hasEmployee(employeeDTO)) {
                        departmentDTO.addEmployee(savedEmployee);
                        employeeRepository.save(EmployeeMapper.toEntity(savedEmployee));
                    }
                }
                catch (DataIntegrityViolationException e){
                    throw new APIRequestException("Exception occurred on attempt to save employee with id: " + savedEmployee.getId() + "\nError message: " + e.getMessage(),e);
                }
            }

            return DepartmentMapper.toDto(departmentRepository.save(DepartmentMapper.toEntity(departmentDTO)));

            } catch (DataIntegrityViolationException e) {
                throw new APIRequestException("Exception occurred on attempt to save department with id: " + id + "\nError message: " + e.getMessage(),e);
        }
    }

    @Transactional
    @Override
    public DepartmentDTO removeEmployee(List<EmployeeDTO> employeeDTOList, int id) {
        try {
            DepartmentDTO departmentDTO = getDepartmentById(id);

            for (EmployeeDTO employeeDTO : employeeDTOList) {
                EmployeeDTO savedEmployee = EmployeeMapper.toDto(employeeRepository.findById(employeeDTO.getId())
                        .orElseThrow(() -> new APIRequestException("Employee not found")));
                try {
                    if(!departmentDTO.hasEmployee(employeeDTO)) {
                        departmentDTO.removeEmployee(savedEmployee);
                        employeeRepository.save(EmployeeMapper.toEntity(savedEmployee));
                    }
                }
                catch (DataIntegrityViolationException e){
                    throw new APIRequestException("Exception occurred on attempt to save employee with id: " + savedEmployee.getId() + "\nError message: " + e.getMessage(),e);
                }
            }

            return DepartmentMapper.toDto(departmentRepository.save(DepartmentMapper.toEntity(departmentDTO)));

        } catch (DataIntegrityViolationException e) {
            throw new APIRequestException("Exception occurred on attempt to save department with id: " + id + "\nError message: " + e.getMessage(),e);
        }    }

    @Override
    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO, int id) {
        try {
            DepartmentDTO updateDepartmentDTO = DepartmentMapper.toDto(departmentRepository.findById(id)
                    .orElseThrow(() -> new APIRequestException("Department not found")));

            if(departmentDTO.getCost_center_code() != null)
                updateDepartmentDTO.setCost_center_code(departmentDTO.getCost_center_code());

            if(departmentDTO.getName() != null)
                updateDepartmentDTO.setName(departmentDTO.getName());

            if(!CollectionUtils.isEmpty(departmentDTO.getEmployees()))
                updateDepartmentDTO.setEmployees(departmentDTO.getEmployees());

            departmentRepository.save(DepartmentMapper.toEntity(updateDepartmentDTO));
            return updateDepartmentDTO;
        }
        catch (DataIntegrityViolationException e) {
            throw new APIRequestException("Exception occurred on attempt to save department with id: " + id + "\nError message: " + e.getMessage(),e);
        }
        catch (IllegalArgumentException e) {
            throw new APIRequestException("Wrong arguments were passed to update department with id: " + id + "\nError message: " + e.getMessage(),e);
        }
    }

    @Override
    public void deleteDepartmentById(int id) {
        departmentRepository.deleteById(id);
    }
}
