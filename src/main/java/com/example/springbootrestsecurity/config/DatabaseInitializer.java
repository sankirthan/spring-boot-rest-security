package com.example.springbootrestsecurity.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import com.example.springbootrestsecurity.jpa.repository.UserRepository;

@Component
public class DatabaseInitializer {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostConstruct
	public void initData() {
		DatabasePopulator databasePopulator = new ResourceDatabasePopulator(new ClassPathResource("/sql/init.sql"));
		DatabasePopulatorUtils.execute(databasePopulator, dataSource);
	}
	
}
