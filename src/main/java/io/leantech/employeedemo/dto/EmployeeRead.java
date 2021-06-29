package io.leantech.employeedemo.dto;

public class EmployeeRead {
	private Long id;
	private Integer salary;
	private PersonRead person;
	
	public EmployeeRead() {}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getSalary() {
		return salary;
	}
	public void setSalary(Integer salary) {
		this.salary = salary;
	}
	public PersonRead getPerson() {
		return person;
	}
	public void setPerson(PersonRead person) {
		this.person = person;
	}
}
