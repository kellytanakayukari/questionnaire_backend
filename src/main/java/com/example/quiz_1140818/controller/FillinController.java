package com.example.quiz_1140818.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_1140818.request.FillinReq;
import com.example.quiz_1140818.response.BasicRes;
import com.example.quiz_1140818.response.FeedbackRes;
import com.example.quiz_1140818.response.StatisticRes;
import com.example.quiz_1140818.service.FillinService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class FillinController {
	
	@Autowired
	FillinService fillinService;

	//新增問卷
	@PostMapping(value = "quiz/fillin")
	public BasicRes fillin(@Valid @RequestBody FillinReq req) throws Exception {
		return fillinService.fillin(req.getUser(), req.getQuizId(), req.getAnswerList());
	}
	

	//透過quizId呼叫name email date
    @GetMapping(value = "quiz/feedback")
    public FeedbackRes feedback(@RequestParam("quizId") int quizId) throws Exception {
        return fillinService.feedback(quizId);
    }
    
    //quiz/feedback/3
    @GetMapping(value = "quiz/feedback/{quizId}")
    public FeedbackRes feedback_1(@PathVariable("quizId") int quizId) throws Exception {
        return fillinService.feedback(quizId);
    }
    
    // 透過 quizId 呼叫統計
    @GetMapping("quiz/statistics")
    public StatisticRes statiststic(@RequestParam("quizId") int quizId) throws Exception {
        return fillinService.statiststic(quizId);
    }

}
