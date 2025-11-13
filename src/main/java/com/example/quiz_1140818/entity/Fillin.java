package com.example.quiz_1140818.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@IdClass(value = FillinId.class)
@Table(name="fillin")
public class Fillin {
	
	@Id
	@Column(name="quiz_id")
	private int quizId;
	
	@Id
	@Column(name="question_id")
	private int questionId;
	
	@Id
	@Column(name="email")
	private String email;
	
	@Column(name="name")
	private String name;

	@Column(name="phone")
	private String phone;
	
	@Column(name="age")
	private int age;

	@Column(name="sex")
	private String sex;

	@Column(name="city")
	private String city;
	
	//物件Answer轉成字串
	@Column(name="answer_str")
	private String answerStr;
	
	@Column(name="fillin_date")
	private LocalDate fillinDate;

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAnswerStr() {
		return answerStr;
	}

	public void setAnswerStr(String answerStr) {
		this.answerStr = answerStr;
	}

	public LocalDate getFillinDate() {
		return fillinDate;
	}

	public void setFillinDate(LocalDate fillinDate) {
		this.fillinDate = fillinDate;
	}
	

}
