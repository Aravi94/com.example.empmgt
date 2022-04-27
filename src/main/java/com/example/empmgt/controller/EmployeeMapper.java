package com.example.empmgt.controller;

import org.springframework.stereotype.Component;

import com.example.empmgt.model.Employee;
import com.example.empmgt.model.EmployeeDTO;

@Component
public class EmployeeMapper {

	public EmployeeDTO toDto(Employee employee) {
        return new EmployeeDTO(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getEmailId());
    }

    public Employee toEmployee(EmployeeDTO employeeDTO) {
        return new Employee(employeeDTO.getFirstName(), employeeDTO.getLastName(), employeeDTO.getEmailId());
    }
    
    public Employee copyDtoToEmp(EmployeeDTO employeeDTO, Employee employee) {
    	employee.setFirstName(employeeDTO.getFirstName());
    	employee.setLastName(employeeDTO.getLastName());
    	employee.setEmailId(employeeDTO.getEmailId());
        return employee;
    }
}
