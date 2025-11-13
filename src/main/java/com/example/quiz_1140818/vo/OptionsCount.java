package com.example.quiz_1140818.vo;

public class OptionsCount {
	
	private int code;
	
	private String optionName;
	
	//給預設值1 (每次都算1筆，就不用特別寫ㄌ)
	private int count = 1;

	public OptionsCount() {
		super();
	}


	public OptionsCount(int code, String optionName) {
		super();
		this.code = code;
		this.optionName = optionName;
	}
	
	



	public OptionsCount(int code, String optionName, int count) {
		super();
		this.code = code;
		this.optionName = optionName;
		this.count = count;
	}


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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	

}
