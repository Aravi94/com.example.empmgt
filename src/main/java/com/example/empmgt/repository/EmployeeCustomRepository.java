package com.example.empmgt.repository;

import java.util.List;

import com.example.empmgt.model.Employee;

public interface EmployeeCustomRepository {

	List <Employee> findLikeFirstName(String firstName);
}
