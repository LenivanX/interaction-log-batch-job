package com.leni.intlogbatch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "employee")
@ToString
public class Employee {
	@Id
	private int id;
	private String name;
	private String dept;
	private float salary;
}
