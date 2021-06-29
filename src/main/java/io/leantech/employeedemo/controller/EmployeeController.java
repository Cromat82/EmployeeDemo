package io.leantech.employeedemo.controller;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.leantech.employeedemo.dto.EmployeeRead;
import io.leantech.employeedemo.dto.EmployeeUpdate;
import io.leantech.employeedemo.dto.PersonRead;
import io.leantech.employeedemo.dto.PositionEmployeesRead;
import io.leantech.employeedemo.model.Employee;
import io.leantech.employeedemo.model.Person;
import io.leantech.employeedemo.model.Position;
import io.leantech.employeedemo.repository.EmployeeRepository;
import io.leantech.employeedemo.repository.PersonRepository;
import io.leantech.employeedemo.repository.PositionRepository;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	
	@Autowired
	EmployeeRepository employeeRepo;
	@Autowired
	PositionRepository positionRepo;
	@Autowired
	PersonRepository personRepo;
	
	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Employee> createEmployee(@ModelAttribute EmployeeUpdate employeeUpdate) {
		Person person = new Person(
			employeeUpdate.getName(), employeeUpdate.getLastName(), employeeUpdate.getAddress(),
			employeeUpdate.getCellphone(), employeeUpdate.getCityName()
		);
		
		Position position = findPosition(employeeUpdate.getPositionId(), employeeUpdate.getPosition());
		
		if (position == null) {
			return ResponseEntity.badRequest().build();
		}
		
		Employee employee = new Employee(person, position, employeeUpdate.getSalary());
		
		personRepo.save(person);
		positionRepo.save(position);
		employeeRepo.save(employee);
		
		return ResponseEntity.ok(employee);
	}
	
	@PostMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Employee> updateEmployee(@ModelAttribute EmployeeUpdate employeeUpdate) {
		Employee employee = employeeRepo.findById(employeeUpdate.getId()).orElse(null);
		if (employee == null) {
			return ResponseEntity.badRequest().build();
		}
		
		Person person = employee.getPerson();
		if (person == null) {
			return ResponseEntity.badRequest().build();
		}
		
		Position position = findPosition(employeeUpdate.getPositionId(), employeeUpdate.getPosition());
		if (position == null) {
			return ResponseEntity.badRequest().build();
		}

		if (employeeUpdate.getName() != null) {
			person.setName(employeeUpdate.getName());
		}
		if (employeeUpdate.getLastName() != null) {
			person.setLastName(employeeUpdate.getLastName());
		}
		if (employeeUpdate.getAddress() != null) {
			person.setAddress(employeeUpdate.getAddress());
		}
		if (employeeUpdate.getCellphone() != null) {
			person.setCellphone(employeeUpdate.getCellphone());
		}
		if (employeeUpdate.getCityName() != null) {
			person.setCityName(employeeUpdate.getCityName());
		}
		if (employeeUpdate.getSalary() != null){
			employee.setSalary(employeeUpdate.getSalary());
		}
		
		employee.setPerson(person);
		employee.setPosition(position);
		
		personRepo.save(person);
		positionRepo.save(position);
		employeeRepo.save(employee);
		
		return ResponseEntity.ok(employee);
	}
	
	@DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteEmployee(@ModelAttribute EmployeeUpdate employeeUpdate) throws JsonProcessingException {
		Employee employee = employeeRepo.findById(employeeUpdate.getId()).orElse(null);
		if (employee == null) {
			return ResponseEntity.badRequest().build();
		}
		
		Person person = employee.getPerson();
		if (person == null) {
			return ResponseEntity.badRequest().build();
		}
		
		employeeRepo.deleteById(employee.getId());
		personRepo.deleteById(person.getId());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", "deleted");
		return ResponseEntity.ok(new ObjectMapper().writeValueAsString(map));
	}
	
	@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Employee>> getEmployees(@RequestParam(required = false) String position, @RequestParam(required = false) String name){
		List<Employee> employees = employeeRepo.findAll();
		
		if (position != null) {
			employees = employees.stream().filter(e -> e.getPosition().getName().equalsIgnoreCase(position))
					.collect(Collectors.toList());
		}
		
		if (name != null) {
			employees = employees.stream().filter(e -> e.getPerson().getName().equalsIgnoreCase(name))
					.collect(Collectors.toList());
		}
		
		return ResponseEntity.ok(employees);
	}
	
	@GetMapping(value = "/positions", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PositionEmployeesRead>> getPositionEmployees() {
		List<Employee> employees = employeeRepo.findAll();
		List<Position> positions = positionRepo.findAll();
		
		List<PositionEmployeesRead> positionList = positions.stream().map((position) -> {
			List<EmployeeRead> employeesList = employees.stream()
					.filter(e -> e.getPosition().getId() == position.getId())
					.map((Employee employee) -> {
						Person person = employee.getPerson();
						EmployeeRead employeeRead = new EmployeeRead();
						employeeRead.setId(employee.getId());
						employeeRead.setSalary(employee.getSalary());
						PersonRead personRead = new PersonRead();
						personRead.setName(person.getName());
						personRead.setLastName(person.getLastName());
						personRead.setAddress(person.getAddress());
						personRead.setCellphone(person.getCellphone());
						personRead.setCityName(person.getCityName());
						employeeRead.setPerson(personRead);
						return employeeRead;
					}).sorted(Comparator.comparingInt(EmployeeRead::getSalary).reversed())
					.collect(Collectors.toList());
			
			PositionEmployeesRead positionRead = new PositionEmployeesRead();
			positionRead.setId(position.getId());
			positionRead.setName(position.getName());
			positionRead.setEmployees(employeesList);
			return positionRead;
		}).collect(Collectors.toList());
		
		return ResponseEntity.ok(positionList);
	}
	
	private Position findPosition(Long id, String name) {
		Position position = null;	
		if (id != null) {
			position = positionRepo.findById(id).orElse(null);
		} else if (!StringUtils.isBlank(name)) {
			name = name.toLowerCase();
			position = positionRepo.findByName(name).orElse(null);
			if (position == null)
				position = new Position(name);
		}
		return position;
	}
}
