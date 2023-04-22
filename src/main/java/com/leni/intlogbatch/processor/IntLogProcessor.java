package com.leni.intlogbatch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.leni.intlogbatch.entity.Employee;
import com.leni.intlogbatch.entity.EmployeeSalData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IntLogProcessor implements ItemProcessor<Employee, EmployeeSalData>{

	@Override
	public EmployeeSalData process(Employee emp) throws Exception {
		log.info("Record: " + emp);
		
		EmployeeSalData empSalData = new EmployeeSalData();
		empSalData.setId(emp.getId());
		empSalData.setName(emp.getName());
		
		if(emp.getSalary()<500) {
			empSalData.setLow_salary(true);
		}
		else {
			empSalData.setLow_salary(false);
		}
		return empSalData;
	}
	
}
