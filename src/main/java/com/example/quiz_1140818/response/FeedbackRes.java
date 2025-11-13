package com.example.quiz_1140818.response;

import java.util.List;

import com.example.quiz_1140818.vo.FeedbackVo;

public class FeedbackRes extends BasicRes {
	
	private List<FeedbackVo> feedbackVo;

	public FeedbackRes() {
		super();
	}

	public FeedbackRes(int code, String message) {
		super(code, message);
	}

	public FeedbackRes(int code, String message, List<FeedbackVo> feedbackVo) {
		super(code, message);
		this.feedbackVo = feedbackVo;
	}

	public List<FeedbackVo> getFeedbackVo() {
		return feedbackVo;
	}

	public void setFeedbackVo(List<FeedbackVo> feedbackVo) {
		this.feedbackVo = feedbackVo;
	}
	
}
