package com.example.quiz_1140818.entity;

import jakarta.persistence.*;

@Entity
@IdClass(QuestionId.class) 
@Table(name = "question")
public class Question {
	
	//問卷id
	@Id
	@Column(name="quiz_id")
	private int quizId;
	
	//問題id
	@Id
	@Column(name="question_id")
	private int questionId;
	
	//問題名稱
	@Column(name="name")
	private String name;
	
	//問題類型
	@Column(name="type")
	private String type;
	
	//是否必填
	@Column(name="need")
	private boolean need;
	
	//是否存在
	@Column(name="exist")
	private boolean exist;
	
	//選項-物件Options轉成字串
	@Column(name="options_str")
	private String optionsStr;

	public Question() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Question(int quizId, int questionId, String name, String type, boolean need, boolean exist,
			String optionsStr) {
		super();
		this.quizId = quizId;
		this.questionId = questionId;
		this.name = name;
		this.type = type;
		this.need = need;
		this.exist = exist;
		this.optionsStr = optionsStr;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isNeed() {
		return need;
	}

	public void setNeed(boolean need) {
		this.need = need;
	}

	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}

	public String getOptionsStr() {
		return optionsStr;
	}

	public void setOptionsStr(String optionsStr) {
		this.optionsStr = optionsStr;
	}
	

}
