package com.leni.intlogbatch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import com.leni.intlogbatch.entity.Employee;
import com.leni.intlogbatch.entity.EmployeeSalData;
import com.leni.intlogbatch.listener.IntLogListener;
import com.leni.intlogbatch.processor.IntLogProcessor;

@Configuration
public class IntLogConfiguration {
	
	@Bean
	public JdbcCursorItemReader<Employee> reader(DataSource dataSource){
		return new JdbcCursorItemReaderBuilder<Employee>()
				.name("db-reader")
				.rowMapper(new BeanPropertyRowMapper<>(Employee.class))
				.sql("select * from employee where dept='MARKETING'")
				.dataSource(dataSource)
				.build();
	}
	
	@Bean
	public IntLogProcessor processor() {
		return new IntLogProcessor();
	}
	
	@Bean
	public JdbcBatchItemWriter<EmployeeSalData> writer(DataSource dataSource){
		return new JdbcBatchItemWriterBuilder<EmployeeSalData>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("insert into emp_sal_data(id,name,low_salary) values(:id,:name,:low_salary)")
				.dataSource(dataSource)
				.build();
	}
	
	@Bean
	public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, 
			DataSource dataSource, JdbcBatchItemWriter<EmployeeSalData> writer) {
		return new StepBuilder("IntLog-step1", jobRepository)
				.<Employee, EmployeeSalData>chunk(10, transactionManager)
				.reader(reader(dataSource))
				.processor(processor())
				.writer(writer)
				.build();
	}
	
	@Bean
	public Job intLogJob(JobRepository jobRepository, IntLogListener listener, Step step1) {
		return new JobBuilder("IntLog-job", jobRepository)
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1)
				.end()
				.build();
	}
}
