package com.example.quiz_1140818.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.quiz_1140818.constants.QuestionType;
import com.example.quiz_1140818.constants.ResCodeMessage;
import com.example.quiz_1140818.dao.QuestionDao;
import com.example.quiz_1140818.dao.QuizDao;
import com.example.quiz_1140818.entity.Question;
import com.example.quiz_1140818.entity.Quiz;
import com.example.quiz_1140818.response.BasicRes;
import com.example.quiz_1140818.response.QuestionListRes;
import com.example.quiz_1140818.response.QuizListRes;
import com.example.quiz_1140818.vo.Options;
import com.example.quiz_1140818.vo.QuestionVo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class QuizService {
	
	// org.slf4j (下面的那個) 新增至日誌訊息
	private Logger logger = LoggerFactory.getLogger(getClass());

	// json跟java物件的轉換
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;

	/**
	 * @Transactional: 1. 使用的於修改資料時(insert/update/delete)且有以下2種情形<br>
	 *                 1.1 同一個方法中有使用多個 Dao 時，例如下面方法中有同時使用 quizDao 和 questionDao<br>
	 *                 1.2 同一個 Dao 有修改多筆的資料，例如下面方法中有使用 questionDao 新增多筆的問題<br>
	 *                 rollbackFor = Exception.class <br>
	 *                 2. 其預設的有效作用範圍是當程式發生 RuntimeException(以及其子類別) 時才會讓資料回朔，所以為了
	 *                 在發生其他 Exception 時也可以讓資料回朔，就要把作用範圍提升到所有例外的父類別: Exception<br>
	 *                 3. 要讓 @Transactional 有效的另一個條件必須要把發生的 Exception 給它 throw 出去
	 */

	// 創建問卷
	@Transactional(rollbackOn = Exception.class)
	public BasicRes create(Quiz quiz, List<QuestionVo> questionVoList) throws Exception {
		try {
			// 檢查question
			BasicRes checkRes = checkQuestion(questionVoList);
			if (checkRes != null) {
				return checkRes;
			}
			// 檢查問卷時間
			checkRes = checkDate(quiz.getStartDate(), quiz.getEndDate());
			if (checkRes != null) {
				return checkRes;
			}
			// 新增問卷
			quizDao.create(quiz.getTitle(), quiz.getDescription(), quiz.getStartDate(), //
					quiz.getEndDate());

			// 取得quiz_Id，因為新增完問卷至DB後，流水號自動產生
			int quizId = quizDao.selectMaxId();

			// 處理Question
			// 將QuestionVo中的List<Options>物件轉optionsStr字串
			for (QuestionVo vo : questionVoList) {
				List<Options> optionsList = vo.getOptionsList();
				if (optionsList == null) {
					optionsList = new ArrayList<>();
				}
				String optionsStr = mapper.writeValueAsString(vo.getOptionsList());
				// 新增問題
				questionDao.create(quizId, vo.getQuestionId(), vo.getName(), vo.getType(), //
						vo.isNeed(), vo.isExist(), optionsStr);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return new BasicRes(//
				ResCodeMessage.SUCCESS.getCode(), //
				ResCodeMessage.SUCCESS.getMessage());
	}

	// 私有方法 檢查Type跟選項
	private BasicRes checkQuestion(List<QuestionVo> questionVoList) {
		for (QuestionVo item : questionVoList) {
			if (!QuestionType.checkAllType(item.getType())) {
				return new BasicRes(//
						// 檢查結果是FALSE表示TYPE不是設定的三種型態之一
						ResCodeMessage.QUESTION_TYPE_ERROR.getCode(), //
						ResCodeMessage.QUESTION_TYPE_ERROR.getMessage());
			}
			// Type是簡答題時，選項內容為空，若T有內容報錯
			if (!QuestionType.checkChoiceType(item.getType())) {
				if (!item.getOptionsList().isEmpty()) {
					return new BasicRes(//
							ResCodeMessage.QUESTION_TYPE_ERROR.getCode(), //
							ResCodeMessage.QUESTION_TYPE_ERROR.getMessage());
				}
			}
			// 單或多選 沒選項 報錯
			if (QuestionType.checkChoiceType(item.getType())) {
				if (item.getOptionsList().isEmpty()) {
					return new BasicRes(//
							ResCodeMessage.QUESTION_TYPE_ERROR.getCode(), //
							ResCodeMessage.QUESTION_TYPE_ERROR.getMessage());
				}
			}
		}
		return null;
	}

	// 私有方法檢查日期
	private BasicRes checkDate(LocalDate startDate, LocalDate endDate) {
		// 檢查問卷日期，開始不可以比結束晚 或 結束不能比開始早
		if (startDate.isAfter(endDate)) {
			return new BasicRes(ResCodeMessage.QUIZ_DATE_ERROR.getCode(), //
					ResCodeMessage.QUIZ_DATE_ERROR.getMessage());
		}
		// 開始日期不可以是今天之前
		if (startDate.isBefore(LocalDate.now())) {
			return new BasicRes(ResCodeMessage.QUIZ_DATE_ERROR.getCode(), //
					ResCodeMessage.QUIZ_DATE_ERROR.getMessage());
		}
		return null;
	}

	// 更新問卷內容
	@Transactional(rollbackOn = Exception.class)
	public BasicRes update(Quiz quiz, List<QuestionVo> questionVoList) throws Exception {

		try {
			// 檢查quizId是否存在於DB
			int quizId = quiz.getId();
			// 檢查問卷時間
			BasicRes checkRes = checkDate(quiz.getStartDate(), quiz.getEndDate());
			if (checkRes != null) {
				return checkRes;
			}
			// 更新問卷quiz
			int res = quizDao.update(quizId, quiz.getTitle(), quiz.getDescription(), quiz.getStartDate(), //
					quiz.getEndDate());
			// 0筆更新等於資料庫沒資料
			if (res == 0) {
				return new BasicRes(//
						ResCodeMessage.NOT_FOUND.getCode(), //
						ResCodeMessage.NOT_FOUND.getMessage());
			}
			// 刪掉問題
			questionDao.deleteById(quizId);
			// 更新選項內容
			for (QuestionVo vo : questionVoList) {
				List<Options> optionsList = vo.getOptionsList();
				if (optionsList == null) {
					optionsList = new ArrayList<>();
				}
				String optionsStr = mapper.writeValueAsString(vo.getOptionsList());
				// 新增問題
				questionDao.create(quizId, vo.getQuestionId(), vo.getName(), vo.getType(), //
						vo.isNeed(), vo.isExist(), optionsStr);
			}

		} catch (Exception e) {
			throw e;
		}
		return new BasicRes(////
				ResCodeMessage.SUCCESS.getCode(), //
				ResCodeMessage.SUCCESS.getMessage());
	}

	// 查看問卷列表
	public QuizListRes getQuizList() {
		List<Quiz> quizList = quizDao.getAll();
		return new QuizListRes( //
				ResCodeMessage.SUCCESS.getCode(), //
				ResCodeMessage.SUCCESS.getMessage(), quizList);
	}

	// 從問卷id取得題目內容
	@Transactional(rollbackOn = Exception.class)
	public QuestionListRes getQuestionList(int quizId) throws Exception {
		// 檢查參數
		if (quizId <= 0) {
			return new QuestionListRes( //
					ResCodeMessage.QUIZ_ID_ERROR.getCode(), //
					ResCodeMessage.QUIZ_ID_ERROR.getMessage());
		}
		// 檢查 quizId是否存在於DB
		if (quizDao.selectCountById(quizId) == 0) { // 0 表示該 quizId 不存在
			return new QuestionListRes( //
					ResCodeMessage.NOT_FOUND.getCode(), //
					ResCodeMessage.NOT_FOUND.getMessage());
		}
		// 用quizId從 question表撈資料
		List<Question> questionList = questionDao.getByQuizId(quizId);
		// 建立QuestionVo List，方便回傳 QuestionListRes資料
		List<QuestionVo> voList = new ArrayList<>();
		for (Question item : questionList) {
			// 將每個optionsStr字串內容轉回物件List<Options>
			try {
				List<Options> optionsList = mapper.readValue(item.getOptionsStr(), //
						new TypeReference<>() {
						});
				// 把每個 Question的屬性值 塞到 QuestionVo 中
				QuestionVo vo = new QuestionVo( //
						item.getQuizId(), item.getQuestionId(), item.getName(), //
						item.getType(), item.isNeed(), item.isExist(), optionsList);
				// 將vo(QuestionVo) 組成新增至 List<QuestionVo>
				voList.add(vo);
			} catch (Exception e) {
				throw e;
			}
		}
		return new QuestionListRes( //
				ResCodeMessage.SUCCESS.getCode(),  //
				ResCodeMessage.SUCCESS.getMessage(), voList);
	}
	
	// 進階搜尋問卷列表(我的前端沒有用到)
	public QuizListRes getQuizList(String title, LocalDate startDate, LocalDate endDate) {
        // 若 title 沒帶值(預設是null) 或 空字串 或 全空白字串 --> 一律轉換成空字串
        // 到時 SQL 中搭配 like% 空字串的 title% 就把逤有 title 的資料撈出來
        if(!StringUtils.hasText(title)) {
            title = "";
        }
        // 轉換沒有帶值的開始日期:將開始日期改成很早之前的一個日期
        if(startDate == null) {
            startDate = LocalDate.of(1970, 1, 1);
        }

        // 轉換沒有帶值的結束日期:將結束日期改成很久之前的一個日期
        if(endDate == null) {
            endDate = LocalDate.of(2999, 12, 31);
        }
        return new QuizListRes( //
        		ResCodeMessage.SUCCESS.getCode(), //
                ResCodeMessage.SUCCESS.getMessage(), //
        		quizDao.getSearch(title, startDate, endDate));
    }
	
	//刪除問卷
	@Transactional(rollbackOn = Exception.class)
	public BasicRes deleteByQuizId(List<Integer> quizIdList) throws Exception {
		try {
		//刪除問卷
		quizDao.deleteByIdIn(quizIdList);
		//刪除問卷題目
		questionDao.deleteByQuizIdIn(quizIdList);}
		catch(Exception e) {
			throw e;
		}
        return new BasicRes( //
        		ResCodeMessage.SUCCESS.getCode(), //
                ResCodeMessage.SUCCESS.getMessage()); //
	}

}
