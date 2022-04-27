package com.example.empmgt;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import com.example.empmgt.controller.EmployeeController;
import com.example.empmgt.model.Employee;
import com.example.empmgt.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;
	
	@MockBean
	EmployeeRepository employeeRepository;
	
	@Autowired
    private EmployeeController employeeController;
	
	Employee emp1 = new Employee(1l, "Dinesh", "Sethi", "dinesh.sethi@gmail.com");
	Employee emp2 = new Employee(2l, "Vignesh", "Lingam", "vignesh.lingam@gmail.com");
	Employee emp3 = new Employee(3l, "Heera", "Kaalidasan", "heera.kaalidasan@gmail.com");
	
	@Test
	@Order(1)
	void contextLoads() {
		assertThat(employeeController, notNullValue());
		assertThat(employeeRepository, notNullValue());
	}
	
	@Test
	@Order(2)
	void getAllEmployees() throws Exception {
	    List<Employee> records = new ArrayList<>(Arrays.asList(emp1, emp2, emp3));
	    
	    Mockito.when(employeeRepository.findAll()).thenReturn(records);
	    
	    mockMvc.perform(MockMvcRequestBuilders
	            .get("/api/v1/employees")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$", hasSize(3)))
	            .andExpect(jsonPath("$[2].firstName", is("Heera")));
	}
	
	@Test
	@Order(3)
	void getEmployeeById() throws Exception {
	    Mockito.when(employeeRepository.findById(emp1.getId())).thenReturn(java.util.Optional.of(emp1));

	    mockMvc.perform(MockMvcRequestBuilders
	            .get("/api/v1/employees/1")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$", notNullValue()))
	            .andExpect(jsonPath("$.firstName", is("Dinesh")));

	}
	
	@Test
	@Order(4)
	void getEmployeeByName() throws Exception {
		List<Employee> records = new ArrayList<>(Arrays.asList(emp2));
		
	    Mockito.when(employeeRepository.findLikeFirstName(emp2.getFirstName())).thenReturn(records);

	    mockMvc.perform(MockMvcRequestBuilders
	            .get("/api/v1/employeename/Vignesh")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$", hasSize(1)))
	            .andExpect(jsonPath("$[0].lastName", is("Lingam")));

	}
	
	/*
	 * @Test
	 * 
	 * @Order(5) void createEmployee() throws Exception { Employee emp = new
	 * Employee("Ramesh", "Srini", "ramesh.srini@gmail.com"); // //
	 * Mockito.when(employeeRepository.save(emp)).thenReturn(emp); // //
	 * MockHttpServletRequestBuilder mockRequest =
	 * MockMvcRequestBuilders.post("/api/v1/employees") //
	 * .contentType(MediaType.APPLICATION_JSON) //
	 * .accept(MediaType.APPLICATION_JSON) //
	 * .content(this.mapper.writeValueAsString(emp)); // ////
	 * mockMvc.perform(mockRequest).andDo(print()); // //
	 * mockMvc.perform(mockRequest) // .andExpect(status().isOk()) //
	 * .andExpect(jsonPath("$", notNullValue())) //
	 * .andExpect(jsonPath("$.firstName", is("Ramesh")));
	 * 
	 * Mockito.when(employeeRepository.save(emp)).thenReturn(emp);
	 * 
	 * mockMvc.perform( MockMvcRequestBuilders .post("/api/v1/employees")
	 * .content(asJsonString(new Employee("Ramesh", "Srini",
	 * "ramesh.srini@gmail.com"))) .contentType(MediaType.APPLICATION_JSON)
	 * .accept(MediaType.APPLICATION_JSON)) .andExpect(status().isCreated())
	 * .andExpect(jsonPath("$.firstName", is("Ramesh"))); }
	 */
	
	/*
	 * @Test
	 * 
	 * @Order(6) void updateEmployee() throws Exception { Employee updEmp = new
	 * Employee(1l, "Deepthi", "Sangs", "deepthi.sangs@gmail.com");
	 * 
	 * Mockito.when(employeeRepository.findById(emp1.getId())).thenReturn(Optional.
	 * of(emp1)); Mockito.when(employeeRepository.save(updEmp)).thenReturn(updEmp);
	 * 
	 * MockHttpServletRequestBuilder mockRequest =
	 * MockMvcRequestBuilders.put("/api/v1/employees/1")
	 * .contentType(MediaType.APPLICATION_JSON) .accept(MediaType.APPLICATION_JSON)
	 * .content(this.mapper.writeValueAsString(updEmp));
	 * 
	 * mockMvc.perform(mockRequest) .andExpect(status().isOk())
	 * .andExpect(jsonPath("$", notNullValue())) .andExpect(jsonPath("$.firstName",
	 * is("Deepthi"))); }
	 */
	
	@Test
	@Order(7)
	void deleteEmployee() throws Exception {
	    Mockito.when(employeeRepository.findById(emp2.getId())).thenReturn(Optional.of(emp2));

	    mockMvc.perform(MockMvcRequestBuilders
	            .delete("/api/v1/employees/2")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk());
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
