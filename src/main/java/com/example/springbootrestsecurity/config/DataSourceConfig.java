package com.example.springbootrestsecurity.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		transactionManagerRef = "transactionManager", 
		entityManagerFactoryRef = "entityManagerFactory", 
		basePackages = "com.example.springbootrestsecurity.jpa.repository")
public class DataSourceConfig {
	
	@Bean
	@Primary
	@ConfigurationProperties("app.datasource")
	public HikariDataSource DataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}
	
	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {		
		Map<String, String> properties = new HashMap<>();
		properties.put("hibernate.format_sql", "true");
		return builder
				.dataSource(DataSource())
				.packages("com.example.springbootrestsecurity.jpa.entity")
				.persistenceUnit("default")
				.properties(properties)
				.build();
	}
	
	@Bean
	@Primary
	public PlatformTransactionManager transactionManager(
			@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
}
