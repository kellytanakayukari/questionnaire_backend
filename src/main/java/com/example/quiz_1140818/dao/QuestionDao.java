package com.example.quiz_1140818.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.quiz_1140818.entity.Question;
import com.example.quiz_1140818.entity.QuestionId;

import jakarta.transaction.Transactional;

@Repository
public interface QuestionDao extends JpaRepository<Question, QuestionId> {

	//塞選項進去
	@Transactional
	@Modifying
	@Query(value ="insert into question (quiz_id, question_id, name, type, need, exist," //
			+ " options_str) values (?1, ?2, ?3, ?4, ?5, ?6, ?7)", nativeQuery=true)
	public void create(int quizId, int questionId, String name, String type, boolean need,
			boolean exist, String optionsStr);
	
	//搜尋全部選項
	@Query(value ="select * from question WHERE quiz_id = ?1", nativeQuery=true)
	public List<Question> getByQuizId(int quizId);
	
	//刪除題目
	@Transactional
	@Modifying
	@Query(value ="DELETE FROM question WHERE quiz_id = ?1;", nativeQuery=true)
	public void deleteById(int quizId);
	
	//刪除問卷中的題目
	@Transactional
	@Modifying
	@Query(value = "delete from question where quiz_id = (?1)", nativeQuery = true)
	public void deleteByQuizIdIn(List<Integer> quizIdList);

	
}
