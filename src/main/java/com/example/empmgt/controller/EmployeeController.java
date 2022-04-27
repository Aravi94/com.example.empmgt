package com.example.empmgt.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.empmgt.exception.ResourceNotFoundException;
import com.example.empmgt.model.Employee;
import com.example.empmgt.model.EmployeeDTO;
import com.example.empmgt.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController implements ApplicationContextAware {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	private ApplicationContext context;
	private EmployeeDTO employeeDTO;
	
	private static final String EMP_NOT_FOUND = "Employee not found for this id ";
	
	@GetMapping("/employees")
	public List<EmployeeDTO> getAllEmployees() {
		return employeeRepository.findAll()
				.stream()
				.map(employeeMapper::toDto)
				.collect(Collectors.toList());
	}
	
	@GetMapping("/employees/{id}")
	public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable(value="id") Long employeeId) 
			throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException(EMP_NOT_FOUND + ":: " + employeeId));
		employeeDTO = employeeMapper.toDto(employee);
		return ResponseEntity.ok().body(employeeDTO);
	}
	
	@GetMapping("/employeename/{name}")
	public List<EmployeeDTO> getEmployeeByName(@PathVariable(value="name") String firstName) 
			throws ResourceNotFoundException {
		List<Employee> resultList = employeeRepository.findLikeFirstName(firstName);
		if(resultList.isEmpty()) {
			throw new ResourceNotFoundException("Employee not found for this name :: " + firstName);
		}
		return resultList.stream().map(employeeMapper::toDto).collect(Collectors.toList());
	}
	
	@PostMapping("/employees")
	public EmployeeDTO createEmployee (@RequestBody EmployeeDTO employeeInput) {
		Employee employee = employeeRepository.save(employeeMapper.toEmployee(employeeInput));
		return employeeMapper.toDto(employee);
	}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable(value = "id") Long employeeId,
			@RequestBody EmployeeDTO employeeInput) throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId)
		        .orElseThrow(() -> new ResourceNotFoundException(EMP_NOT_FOUND + ":: " + employeeId));
		employeeMapper.copyDtoToEmp(employeeInput, employee);
        final Employee updatedEmployee = employeeRepository.save(employee);
        employeeDTO = employeeMapper.toDto(updatedEmployee);
        return ResponseEntity.ok(employeeDTO);
	}
	
	@DeleteMapping("/employees/{id}")
	public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId) 
			throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId)
			       .orElseThrow(() -> new ResourceNotFoundException(EMP_NOT_FOUND + ":: " + employeeId));
		employeeRepository.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
	
	@PostMapping("/shutdownContext")
    public void shutdownContext() {
        ((ConfigurableApplicationContext) context).close();
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

}
