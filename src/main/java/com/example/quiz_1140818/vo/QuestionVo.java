package com.example.quiz_1140818.vo;

import java.util.List;

import com.example.quiz_1140818.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuestionVo {

	//問卷id
	private int quizId;
	
	//問題id
	@Min(value = 1, message= ConstantsMessage.QUESTION_ID_ERROR)
	private int questionId;
	
	//問題名稱
	@NotBlank(message= ConstantsMessage.QUESTION_NAME_ERROR)
	private String name;
	
	//問題類型
	@NotBlank(message= ConstantsMessage.QUESTION_TYPE_ERROR)
	private String type;
	
	//是否必填
	@NotNull(message= ConstantsMessage.QUESTION_NEED_ERROR)
	private boolean need;
	
	//是否存在
	@NotNull(message= ConstantsMessage.QUESTION_EXIST_ERROR)
	private boolean exist;

	// 選項-物件Options轉成字串 簡答題沒有選項，不限制
	private List<Options> optionsList;

	public QuestionVo() {
		super();

	}

	public QuestionVo(int quizId,  int questionId, String name, String type, boolean need, //
			boolean exist, List<Options> optionsList) {
		super();
		this.quizId = quizId;
		this.questionId = questionId;
		this.name = name;
		this.type = type;
		this.need = need;
		this.exist = exist;
		this.optionsList = optionsList;
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
	

}
