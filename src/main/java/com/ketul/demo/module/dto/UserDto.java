package com.ketul.demo.module.dto;

public class UserDto {

	private String name;
	
	private String age;
	
	private String email;
	
	private String pass;

	public UserDto() {
		super();
	}

	public UserDto(String name, String age, String email, String pass) {
		super();
		this.name = name;
		this.age = age;
		this.email = email;
		this.pass = pass;
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
		return "User [name=" + name + ", age=" + age + ", email=" + email + ", pass=" + pass + "]";
	}
}
