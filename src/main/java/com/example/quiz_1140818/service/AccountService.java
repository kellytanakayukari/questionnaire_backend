package com.example.quiz_1140818.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.quiz_1140818.constants.ResCodeMessage;
import com.example.quiz_1140818.dao.AccountDao;
import com.example.quiz_1140818.entity.Account;
import com.example.quiz_1140818.response.BasicRes;

@Service
public class AccountService {
	
	//encoder密碼加密物件
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Autowired
	private AccountDao dao;
	
	public BasicRes addInfo(String account, String password) {
		
		// 已經有帳號
		int count = dao.selectCountByAccount(account);
		if (count != 0) {
			return new BasicRes(//
					ResCodeMessage.ACCOUNT_EXIST.getCode(), //
					ResCodeMessage.ACCOUNT_EXIST.getMessage());
		}
		try {
			// encoder.encode()將密碼加密
			dao.addInfo(account, encoder.encode(password));
			return new BasicRes(//
					ResCodeMessage.SUCCESS.getCode(), //
					ResCodeMessage.SUCCESS.getMessage());
		}catch(Exception e) {
			// 發生 Exception 時，可以有以下2種處理方式:
			// 1. 固定的回覆訊息，但真正錯誤原因無法顯示
			// 2. 將 catch 到的例外(Exception)拋出(throw)，再由自定義的類別
			//    GlobalExceptionHandler 寫入(回覆)真正的錯誤訊息
			throw e;
		}
	}
	
	public BasicRes login(String account, String password) {
		
		Account data = dao.selectByAccount(account);
		
		//有沒有資料
		if(data == null) {
			return new BasicRes(//
					ResCodeMessage.NOT_FOUND.getCode(), //
					ResCodeMessage.NOT_FOUND.getMessage());
		}
		
		//比對密碼
		if (!encoder.matches(password, data.getPassword())) {
			return new BasicRes(//
					ResCodeMessage.PASSWORD_MISMATCH.getCode(), //
					ResCodeMessage.PASSWORD_MISMATCH.getMessage());
		}
		return new BasicRes(//
				ResCodeMessage.SUCCESS.getCode(), //
				ResCodeMessage.SUCCESS.getMessage());
	}

}
