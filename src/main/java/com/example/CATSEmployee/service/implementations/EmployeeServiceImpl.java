package com.example.CATSEmployee.service.implementations;

import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.exception.APIRequestException;
import com.example.CATSEmployee.mapper.EmployeeMapper;
import com.example.CATSEmployee.repository.EmployeeRepository;
import com.example.CATSEmployee.service.interfaces.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(int id) {
        return EmployeeMapper.toDto(employeeRepository.findById(id)
                .orElseThrow(() -> new APIRequestException("Department not found")));
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        employeeRepository.save(EmployeeMapper.toEntity(employeeDTO));
        return employeeDTO;
    }

    @Override
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO, int id) {
        try {
            EmployeeDTO updateEmployeeDTO = EmployeeMapper.toDto(employeeRepository.findById(id)
                    .orElseThrow(() -> new APIRequestException("Department not found")));
            updateEmployeeDTO.setFirstName(employeeDTO.getFirstName());
            updateEmployeeDTO.setLastName(employeeDTO.getLastName());
            updateEmployeeDTO.setEmail(employeeDTO.getEmail());
            updateEmployeeDTO.setOperational_head(employeeDTO.getOperational_head());
            updateEmployeeDTO.setDirect_supervisor_id(employeeDTO.getDirect_supervisor_id());
            updateEmployeeDTO.setSubordinate_id(employeeDTO.getSubordinate_id());
            updateEmployeeDTO.setDepartment(employeeDTO.getDepartment());

            employeeRepository.save(EmployeeMapper.toEntity(updateEmployeeDTO));
            return updateEmployeeDTO;
        }
        catch (IllegalArgumentException e) {
            throw new APIRequestException(e.getMessage(), e);
        }
        catch (ResponseStatusException e) {
            throw new APIRequestException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteEmployeeById(int id) {
        employeeRepository.deleteById(id);
    }
}
