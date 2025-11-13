package com.example.quiz_1140818.constants;

public class ConstantsMessage {
	
	/**
	 * 純字串的訊息可放這
	 * final: 將參數定義為常數(固定不變的數)，常數必須要有預設值，參數的命名規則是全大寫
	 *       ，用底線串接不同的字<br>
	 * static: 將該變數變成全域(只有一個記憶體位址)，可直接使用類別名稱來呼叫使用<br>
	 *         例如: ConstantsMessage.PARAM_ACCOUNT_ERROR
	 */

	public static final String PARAM_PASSWORD_ERROR = "Param password error!!"; 
	public static final String PARAM_ACCOUNT_ERROR ="Param account error!!";

	//問卷
	public static final String QUIZ_TITLE_ERROR ="Quiz title error!!";
	public static final String QUIZ_DESCRIPTION_ERROR ="Quiz description error!!";
	public static final String QUIZ_STARTDATE_ERROR ="Quiz startDate error!!";
	public static final String QUIZ_ENDDATE_ERROR ="Quiz endDate error!!";
	public static final String QUIZ_ERROR ="Quiz error!!";
	public static final String QUIZ_DATE_ERROR ="Quiz error!!";
	public static final String QUIZ_ID_LIST_IS_EMPTY ="Quiz id list is empty!!";
	public static final String QUIZ_ID_ERROR ="Quiz id error!!";
	
	//問題
	public static final String QUESTION_ID_ERROR ="Question id error!!";
	public static final String QUESTION_NAME_ERROR ="Question name error!!";
	public static final String QUESTION_TYPE_ERROR ="Question type error!!";
	public static final String QUESTION_NEED_ERROR ="Question need error!!";
	public static final String QUESTION_EXIST_ERROR ="Question exist error!!";
	public static final String QUESTION_OPTIONS_ERROR ="Question options error!!";
	
	//選項
	public static final String QUESTION_OPTIONS_CODE_ERROR ="Question options code error!!";
	public static final String QUESTION_OPTIONS_NAME_ERROR ="Question options name error!!";
	
	//使用者
	public static final String USER_NAME_ERROR ="user name error";
	public static final String USER_EMAIL_ERROR ="user email error";
	public static final String USER_AGE_ERROR ="user age error";
	public static final String USER_INFO_IS_NULL ="user info is null";
}

