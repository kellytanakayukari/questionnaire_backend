package com.example.quiz_1140818.request;

import com.example.quiz_1140818.constants.ConstantsMessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class BasicReq {

	/**
	 * @NotBlank(message = "Account format error!!")<br>
	 * ConstantsMessage 參數檢查only~
	 * @NotBlank: 表示限制字串 account 不能是 null 或 空字串 或 全空白字串<br>
	 * message 後面的字串表示當違反 @NotBlank 的限制時會得到的提示訊息
	 * @Pattern 代表可使用正規表達式的限制
	 * message 只能接收字串（String），不能是物件或其他型別。<br>
	 */
	@NotBlank(message= ConstantsMessage.PARAM_ACCOUNT_ERROR)
	@Pattern(regexp="\\w{3,8}", message= ConstantsMessage.PARAM_ACCOUNT_ERROR)
	private String account;
	
	@NotBlank(message= ConstantsMessage.PARAM_PASSWORD_ERROR)
	@Pattern(regexp="\\w{8,16}",message= ConstantsMessage.PARAM_ACCOUNT_ERROR)
	private String password;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
