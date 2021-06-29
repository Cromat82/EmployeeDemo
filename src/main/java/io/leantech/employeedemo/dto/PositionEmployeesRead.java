package io.leantech.employeedemo.dto;

import java.util.List;

public class PositionEmployeesRead {
	private Long id;
	private String name;
	private List<EmployeeRead> employees;
	
	public PositionEmployeesRead() {}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<EmployeeRead> getEmployees() {
		return employees;
	}
	public void setEmployees(List<EmployeeRead> employees) {
		this.employees = employees;
	}	
}
