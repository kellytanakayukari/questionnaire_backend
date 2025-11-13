package com.example.quiz_1140818.entity;

import com.example.quiz_1140818.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class User {

	@NotBlank(message = ConstantsMessage.USER_NAME_ERROR)
	private String name;

	private String phone;

	@NotBlank(message = ConstantsMessage.USER_EMAIL_ERROR)
	private String email;

	@Min(value = 1, message = ConstantsMessage.USER_AGE_ERROR)
	private int age;

	private String sex;

	private String city;

	public User() {
		super();
	}

	public User(String name, String phone,String email, int age, String sex, String city) {
		super();
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.sex = sex;
		this.city = city;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
