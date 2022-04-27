package com.example.empmgt.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.empmgt.model.Employee;

@Repository
@Transactional(readOnly = true)
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {

	@PersistenceContext
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> findLikeFirstName(String firstName) {
		 Query query = entityManager.createNativeQuery("SELECT * FROM employees " +
	                "WHERE first_name LIKE ?", Employee.class);
	        query.setParameter(1, "%" + firstName + "%");

	        return query.getResultList();
	}

}
