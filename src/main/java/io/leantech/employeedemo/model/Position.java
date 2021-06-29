package io.leantech.employeedemo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Position {

	private @Id @GeneratedValue Long id;
	
	private String name;
	
	public Position() {}
	
	public Position(String name){
		this.name = name;
	}
	
	
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
