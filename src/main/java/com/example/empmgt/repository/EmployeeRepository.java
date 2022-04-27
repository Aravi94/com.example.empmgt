package com.example.empmgt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.empmgt.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeCustomRepository {

}
