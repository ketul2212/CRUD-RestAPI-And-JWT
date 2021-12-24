package com.ketul.demo.module;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description = "About User")
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Size(min = 3, message = "Your name atleast 3 characters")
	@Pattern(regexp = "(^[A-Z])\\w+", message = "Your Name should contain 1st capital letter")
	@ApiModelProperty(notes = "Your name atleast 3 characters and it contains 1st letter as capital")
	private String name;
	
	@Min(value = 10, message = "Your age is > 10")
	@Max(value = 30, message = "Your age is < 30")
	@ApiModelProperty(notes = "Your age is > 10 and < 30")
	private String age;

	@Column(unique = true, nullable = true)
	private String email;
	
	private String pass;
	
	public User() {
		super();
	}

	public User( String name, String age, String email, String pass) {
		super();
		this.name = name;
		this.age = age;
		this.email = email;
		this.pass = pass;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", email=" + email + ", pass=" + pass + "]";
	}
}
