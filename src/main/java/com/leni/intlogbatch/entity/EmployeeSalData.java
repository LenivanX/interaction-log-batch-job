package com.leni.intlogbatch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "emp_sal_data")
@ToString
public class EmployeeSalData {
	@Id
	private int id;
	private String name;
	private boolean low_salary;
}
