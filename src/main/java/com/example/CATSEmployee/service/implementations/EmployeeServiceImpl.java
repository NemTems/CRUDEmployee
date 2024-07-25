package com.example.CATSEmployee.service.implementations;

import com.example.CATSEmployee.DTO.concrete.EmployeeDTO;
import com.example.CATSEmployee.exception.APIRequestException;
import com.example.CATSEmployee.mapper.EmployeeMapper;
import com.example.CATSEmployee.repository.EmployeeRepository;
import com.example.CATSEmployee.service.interfaces.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
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

    @Transactional
    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        try {
            // Saved to get an id from db
            EmployeeDTO savedEmployeeDTO = EmployeeMapper.toDto(employeeRepository.save(EmployeeMapper.toEntity(employeeDTO)));

            if (savedEmployeeDTO.getDirect_supervisor_id() == 0) {
                savedEmployeeDTO.setOperational_head(true);
            }
            else { // Sets the operational head to false and adds the main object as a subordinate to a referenced Employee
                savedEmployeeDTO.setOperational_head(false);
                EmployeeDTO savedDirectSupervisor = getEmployeeById(savedEmployeeDTO.getDirect_supervisor_id());
                addSubordinates(List.of(savedEmployeeDTO), savedDirectSupervisor.getId());
            }
            addSubordinates(savedEmployeeDTO.getSubordinates(), savedEmployeeDTO.getId());
            return savedEmployeeDTO;
        }
        catch (DataIntegrityViolationException e) {
            throw new APIRequestException("Exception occurred on attempt to create an employee" + "\nError message: " + e.getMessage(),e);
        }
    }

    @Transactional
    @Override
    public EmployeeDTO addSubordinates(List<EmployeeDTO> subordinates, int id) {
        try {
            if (CollectionUtils.isEmpty(subordinates)) return null;

            EmployeeDTO foundEmployee = getEmployeeById(id);

            for (EmployeeDTO subordinate : subordinates) { // Sets the operational head to false and direct supervisor to each subordinate of the main object
                EmployeeDTO savedSubordinate = EmployeeMapper.toDto(employeeRepository.findById(subordinate.getId())
                        .orElseThrow(() -> new APIRequestException("Subordinate not found")));
                try {
                    if(!foundEmployee.hasSubordinate(savedSubordinate)) {
                        foundEmployee.addSubordinate(savedSubordinate);
                        employeeRepository.save(EmployeeMapper.toEntity(savedSubordinate));
                    }
                }
                catch (DataIntegrityViolationException e) {
                    throw new APIRequestException("Exception occurred on attempt to save subordinate with id: " + savedSubordinate.getId() + "\nError message: " + e.getMessage(),e);
                }
            }

            return EmployeeMapper.toDto(employeeRepository.save(EmployeeMapper.toEntity(foundEmployee)));
        }catch (DataIntegrityViolationException e) {
            throw new APIRequestException("Exception occurred on attempt to save employee with id: " + id + "\nError message: " + e.getMessage(),e);
        }
    }

    @Transactional
    @Override
    public EmployeeDTO removeSubordinates(List<EmployeeDTO> subordinates, int id) {
        try {
            if (CollectionUtils.isEmpty(subordinates)) return null;

            EmployeeDTO foundEmployee = getEmployeeById(id);

            for (EmployeeDTO subordinate : subordinates) {
                EmployeeDTO savedSubordinate = EmployeeMapper.toDto(employeeRepository.findById(subordinate.getId())
                        .orElseThrow(() -> new APIRequestException("Subordinate not found")));
                try {
                    if(!foundEmployee.hasSubordinate(savedSubordinate)) {
                        foundEmployee.removeSubordinate(savedSubordinate);
                        employeeRepository.save(EmployeeMapper.toEntity(savedSubordinate));
                    }
                } catch (DataIntegrityViolationException e) {
                    throw new APIRequestException("Exception occurred on attempt to save subordinate with id: " + savedSubordinate.getId() + "\nError message: " + e.getMessage(), e);
                }
            }

            return EmployeeMapper.toDto(employeeRepository.save(EmployeeMapper.toEntity(foundEmployee)));
        }
        catch (DataIntegrityViolationException e) {
            throw new APIRequestException("Exception occurred on attempt to save employee with id: " + id + "\nError message: " + e.getMessage(),e);
        }
    }


    @Override
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO, int id) {
        try {
            EmployeeDTO updateEmployeeDTO = getEmployeeById(id);

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

            if(employeeDTO.getDepartment_id() != 0)
                updateEmployeeDTO.setDepartment_id(employeeDTO.getDepartment_id());

            employeeRepository.save(EmployeeMapper.toEntity(updateEmployeeDTO));
            return updateEmployeeDTO;
        }
        catch (DataIntegrityViolationException e) {
            throw new APIRequestException("Exception occurred on attempt to save employee with id: " + id + "\nError message: " + e.getMessage(),e);
        }
        catch (IllegalArgumentException e) {
            throw new APIRequestException("Wrong arguments were passed to update employee with id: " + id + "\nError message: " + e.getMessage(),e);
        }
    }

    @Override
    public void deleteEmployeeById(int id) {
        try {
            employeeRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new APIRequestException("Exception occurred on attempt to delete employee with id: " + id + "\nError message: " + e.getMessage(),e);
        }
    }
}
