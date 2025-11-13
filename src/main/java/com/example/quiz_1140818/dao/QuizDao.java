package com.example.quiz_1140818.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.quiz_1140818.entity.Quiz;

import jakarta.transaction.Transactional;

@Repository
public interface QuizDao extends JpaRepository<Quiz, Integer> {

	//新增問卷
	@Transactional
	@Modifying
	@Query(value = "insert into quiz (title, description, start_date, end_date) " //
			+ " values (?1, ?2, ?3, ?4)", nativeQuery = true)
	public void create(String title, String description, LocalDate startDate, //
			LocalDate endDate);

	//從最小id篩選
	@Query(value = "select max(id) from quiz", nativeQuery = true)
	public int selectMaxId();

	//查詢問卷
	@Query(value = "SELECT * FROM quiz", nativeQuery = true)
	public List<Quiz> getAll();
	
	//查詢問卷(進階)
	@Query(value = "select * from quiz where title like %?1% and start_date >= ?2 and"
            + " end_date <=?3", nativeQuery = true)
    public List<Quiz> getSearch(String title, LocalDate startDate, LocalDate endDate);
	
	//id查詢問卷
    @Query(value = "SELECT * FROM quiz WHERE id = ?1", nativeQuery = true)
    public Quiz getQuizById(int id);
    
	//計算該問卷id出現的次數
	@Query(value = "select count(id) from quiz where id = ?1", nativeQuery = true)
	public int selectCountById(int id);
	
	//更新問卷
	@Transactional
	@Modifying
	@Query(value = "update quiz set title =?2, description = ?3, start_date = ?4,"
			+ " end_date = ?5 where id = ?1", nativeQuery = true)
	public int update(int id, String title, String description, LocalDate startDate, //
			LocalDate endDate);
	
	//刪除問卷
	@Transactional
	@Modifying
	@Query(value = "delete from quiz where id in (?1)", nativeQuery = true)
	public void deleteByIdIn(List<Integer> quizIdList);
	
}
