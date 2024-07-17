package com.example.CATSEmployee.service.implementations;

import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.exception.APIRequestException;
import com.example.CATSEmployee.mapper.EmployeeMapper;
import com.example.CATSEmployee.models.concrete.Employee;
import com.example.CATSEmployee.repository.EmployeeRepository;
import com.example.CATSEmployee.service.interfaces.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
                .orElseThrow(() -> new APIRequestException("Employee not found")));
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        try {
            Employee employee = employeeRepository.save(EmployeeMapper.toEntity(employeeDTO));
            EmployeeDTO savedEmployeeDTO = EmployeeMapper.toDto(employee);

            if (!savedEmployeeDTO.getSubordinates().isEmpty()) { // Sets the operational head to false and direct supervisor to each subordinate of the main object
                for(EmployeeDTO subordinate : savedEmployeeDTO.getSubordinates()) {
                    EmployeeDTO savedSubordinate = getEmployeeById(subordinate.getId());
                    savedSubordinate.setOperational_head(false);
                    savedSubordinate.setDirect_supervisor_id(savedEmployeeDTO.getId());
                    employeeRepository.save(EmployeeMapper.toEntity(savedSubordinate));
                }
            }

            if (savedEmployeeDTO.getDirect_supervisor_id() == 0) {
                savedEmployeeDTO.setOperational_head(true);
            }
            else { // Sets an operational head to false and adds the main object as a subordinate to a referenced Employee
                savedEmployeeDTO.setOperational_head(false);
                EmployeeDTO savedDirectSupervisor = getEmployeeById(savedEmployeeDTO.getDirect_supervisor_id());
                List<EmployeeDTO> newSubordinates = new ArrayList<>(savedDirectSupervisor.getSubordinates());
                newSubordinates.add(EmployeeDTO.builder()
                        .id(savedEmployeeDTO.getId())
                        .build());
                savedDirectSupervisor.setSubordinates(newSubordinates);
                updateEmployee(savedDirectSupervisor, savedDirectSupervisor.getId());
            }
            return savedEmployeeDTO;
        }
        catch (DataIntegrityViolationException e) {
            throw new APIRequestException(e.getMessage(),e);
        }
    }


    @Override
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO, int id) {
        try {
            EmployeeDTO updateEmployeeDTO = EmployeeMapper.toDto(employeeRepository.findById(id)
                    .orElseThrow(() -> new APIRequestException("Employee not found")));

            if(employeeDTO.getFirstName() != null)
                updateEmployeeDTO.setFirstName(employeeDTO.getFirstName());

            if(employeeDTO.getLastName() != null)
                updateEmployeeDTO.setLastName(employeeDTO.getLastName());

            if(employeeDTO.getEmail() != null)
                updateEmployeeDTO.setEmail(employeeDTO.getEmail());

            if(employeeDTO.getOperational_head() != null)
                updateEmployeeDTO.setOperational_head(employeeDTO.getOperational_head());

            if(employeeDTO.getDirect_supervisor_id() != 0)
                updateEmployeeDTO.setDirect_supervisor_id(employeeDTO.getDirect_supervisor_id());

            if(!Objects.isNull(employeeDTO.getSubordinates()))
                updateEmployeeDTO.setSubordinates(employeeDTO.getSubordinates());

            if(employeeDTO.getDepartment_id() != 0)
                updateEmployeeDTO.setDepartment_id(employeeDTO.getDepartment_id());

            employeeRepository.save(EmployeeMapper.toEntity(updateEmployeeDTO));
            return updateEmployeeDTO;
        }
        catch (DataIntegrityViolationException | IllegalArgumentException e) {
            throw new APIRequestException(e.getMessage() ,e);
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
