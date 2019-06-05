package com.example.springbootrestsecurity.jpa.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@ApiModel
@Entity
@Table(name="customer")
public class Customer {
	
	@ApiModelProperty(value = "id", position = 1)
	@Id
	@Column(name="customer_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ApiModelProperty(value = "first name", required=true, position = 2)
	@Column(name="first_name")
	private String firstName;
	
	@ApiModelProperty(value = "last name", required=true, position = 3)
	@Column(name="last_name")
	private String lastName;

	public Customer() {
	}
	
	public Customer(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
