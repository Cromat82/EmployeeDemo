package io.leantech.employeedemo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import io.leantech.employeedemo.model.Employee;

@Component
@RepositoryRestResource(exported=false)
public interface EmployeeRepository extends CrudRepository<Employee, Long>{
	@Override
	List<Employee> findAll();
}
