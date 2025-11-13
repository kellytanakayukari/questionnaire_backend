package com.example.quiz_1140818.vo;

import java.util.List;


public class QuestionAnswerVo {
	
	private int quizId;
	
	private int questionId;
	
	private String name;
	
	private String type;
	
	private boolean need;
	
	private boolean exist;

	private List<Options> optionsList;
	
	private String textAnswer;
	
	private int radioAnswer;
	
	

	public QuestionAnswerVo() {
		super();
	}
	
	

	public QuestionAnswerVo(int quizId, int questionId, String name, //
			String type, boolean need, boolean exist
			) {
		super();
		this.quizId = quizId;
		this.questionId = questionId;
		this.name = name;
		this.type = type;
		this.need = need;
		this.exist = exist;
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

	public List<Options> getOptionsList() {
		return optionsList;
	}

	public void setOptionsList(List<Options> optionsList) {
		this.optionsList = optionsList;
	}

	public String getTextAnswer() {
		return textAnswer;
	}

	public void setTextAnswer(String textAnswer) {
		this.textAnswer = textAnswer;
	}

	public int getRadioAnswer() {
		return radioAnswer;
	}

	public void setRadioAnswer(int radioAnswer) {
		this.radioAnswer = radioAnswer;
	}

}
