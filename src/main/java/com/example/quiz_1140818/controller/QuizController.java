package com.example.quiz_1140818.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_1140818.request.CreateUpdateReq;
import com.example.quiz_1140818.request.DeleteReq;
import com.example.quiz_1140818.request.SearchReq;
import com.example.quiz_1140818.response.BasicRes;
import com.example.quiz_1140818.response.QuestionListRes;
import com.example.quiz_1140818.response.QuizListRes;
import com.example.quiz_1140818.service.QuizService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class QuizController {
	
	@Autowired
	private QuizService quizService;

	//新增問卷
	@PostMapping(value = "quiz/create")
	public BasicRes create(@Valid @RequestBody CreateUpdateReq req) throws Exception {
		return quizService.create(req.getQuiz(), req.getQuestionVoList());
	}
	
	//查看問卷列表
	@GetMapping(value = "quiz/list")
    public QuizListRes getQuizList() {
        return quizService.getQuizList();
    }
	
	@PostMapping(value = "quiz/search")
	//查看問卷(進階)
	public QuizListRes getQuizList(@RequestBody SearchReq req) {
		return quizService.getQuizList(req.getTitle(),req.getStartDate(),req.getEndDate());
	}
	
	//更新問卷
	@PostMapping(value = "quiz/update")
	public BasicRes update(@Valid @RequestBody CreateUpdateReq req) throws Exception {
		return quizService.update(req.getQuiz(), req.getQuestionVoList());
	}
	
	// 取得指定問卷的題目列表
	@GetMapping(value = "quiz/question_list")
	public QuestionListRes getQuestionList(@RequestParam("quizId") int quizId) throws Exception {
	    return quizService.getQuestionList(quizId);
	}
	
	//刪除問卷
	@PostMapping(value = "quiz/delete")
	public BasicRes deleteByQuizId(@Valid @RequestBody DeleteReq req) throws Exception {
		return quizService.deleteByQuizId(req.getQuizIdList());
	}
}
