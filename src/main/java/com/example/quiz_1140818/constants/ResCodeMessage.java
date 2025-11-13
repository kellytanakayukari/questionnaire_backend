package com.example.quiz_1140818.constants;

public enum ResCodeMessage {
	
	/**
	 * 定義固定且有意義的常數的型別，常數像物件擁有欄位與方法。
	 * 表示「狀態碼、錯誤碼、角色類別」等有結構的資料，例如一個錯誤代碼同時包含 code 與 message。
	 * 型別安全、結構清晰、可擴充性強，比起單純字串更能表達語意。
	*/
	SUCCESS(200,"success!"), //
	ADD_INFO_FAILED(400,"Add info error!!"), //
	NOT_FOUND(404,"not found"),//
	PARAM_ACCOUNT_ERROR(400,"Param account error!!"),//
	PARAM_PASSWORD_ERROR(400,"Param password error!!"), //
	PASSWORD_MISMATCH(400,"password mismatch!!"),//
	ACCOUNT_EXIST(400,"帳號已存在!!"),
    QUESTION_TYPE_ERROR(400,ConstantsMessage.QUESTION_TYPE_ERROR),
    QUIZ_DATE_ERROR(400,ConstantsMessage.QUIZ_DATE_ERROR),
    QUIZ_ID_ERROR(400,"quiz id error!!"),
	QUESTION_TYPE_OPTIONS_MISMATCH(400,"Question type and options mismatch!!"),
	RADIO_IS_NEED(400,"Radio is need!"),
	BOXBOOLEAN_IS_NEED(400,"boolean is need!"),
	QUESTION_OPTION_MISMATCH(400,"Question option mismatch!!"),
	CHECKBOX_IS_NEED(400,"CHECKBOX_IS_NEED!!"),
	TEXT_IS_NEED(400,"Text is need!");
	
	//屬性
	private int code;
	
	private String message;

	//建構方法
	private ResCodeMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	//get、set
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
