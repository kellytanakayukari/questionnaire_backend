package com.example.quiz_1140818.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz_1140818.entity.Account;

@Repository
public interface AccountDao extends JpaRepository<Account, String> {
	
	//新增資訊
	@Modifying
	@Transactional
	@Query(value = "insert into account (account, password) "
			+ " values (?1 , ?2) ", nativeQuery = true)
	public void addInfo(String account, String password);
	
	//判斷帳號是否存在，有東西是1
	@Query(value = "select count(account) from account where account = ?1", nativeQuery = true)
	public int selectCountByAccount(String account);
	
	//透過帳號查詢所有資料
	@Query(value = "select * from account where account = ?1", nativeQuery = true)
	public Account selectByAccount(String account);

}
