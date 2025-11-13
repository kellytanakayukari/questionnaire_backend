package com.example.quiz_1140818.vo;

import com.example.quiz_1140818.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Options {
	
	//vo為存放值的意思
	
	//選項id
	@Min(value = 1, message= ConstantsMessage.QUESTION_OPTIONS_CODE_ERROR)
	private int code;
	
	//選項名稱
	@NotBlank(message= ConstantsMessage.QUESTION_OPTIONS_NAME_ERROR)
	private String optionName;
	
	//多選是否有選到
	private boolean boxBoolean;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public boolean isBoxBoolean() {
		return boxBoolean;
	}

	public void setBoxBoolean(boolean boxBoolean) {
		this.boxBoolean = boxBoolean;
	}

}
