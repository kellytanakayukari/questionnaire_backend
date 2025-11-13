package com.example.quiz_1140818.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.quiz_1140818.constants.QuestionType;
import com.example.quiz_1140818.constants.ResCodeMessage;
import com.example.quiz_1140818.dao.FillinDao;
import com.example.quiz_1140818.dao.QuestionDao;
import com.example.quiz_1140818.dao.QuizDao;
import com.example.quiz_1140818.entity.Fillin;
import com.example.quiz_1140818.entity.Question;
import com.example.quiz_1140818.entity.Quiz;
import com.example.quiz_1140818.entity.User;
import com.example.quiz_1140818.response.BasicRes;
import com.example.quiz_1140818.response.FeedbackRes;
import com.example.quiz_1140818.response.StatisticRes;
import com.example.quiz_1140818.vo.Answer;
import com.example.quiz_1140818.vo.FeedbackVo;
import com.example.quiz_1140818.vo.Options;
import com.example.quiz_1140818.vo.OptionsCount;
import com.example.quiz_1140818.vo.QuestionAnswerVo;
import com.example.quiz_1140818.vo.QuestionCountVo;
import com.example.quiz_1140818.vo.StatisticVo;
import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class FillinService {

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private FillinDao fillinDao;

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;

	@Transactional(rollbackOn = Exception.class)
	public BasicRes fillin(User user, int quizId, List<Answer> answerList) throws Exception {
		// 檢查答案
		// 1.取出問卷所有問題
		List<Question> questionList = questionDao.getByQuizId(quizId);
		for (Question question : questionList) {
			// 2.檢查必填是否有答案
			if (question.isNeed()) {
				BasicRes res = checkRequiredAnswer(question.getQuestionId(), answerList, //
						question.getType());
				if (res != null) {
					return res;
				}
			}
			// 3.答案中的選項 是否與 問卷中選項 一樣
			List<Options> reqOptionList = new ArrayList<>();
			for (Answer answer : answerList) {
				if (question.getQuestionId() == answer.getQuestionId()) {
					reqOptionList = answer.getOptionsList();
					break;
				}
			}
			try {
				BasicRes res = checkOptions(question.getOptionsStr(), reqOptionList);
				if (res != null) {
					return res;
				}
			} catch (Exception e) {
				throw e;
			}
		}
		// 4.寫答案可能有多筆所以要加 @Transactional 確保一致性
		try {
			for (Answer item : answerList) {
				fillinDao.fillin(quizId, item.getQuestionId(), user.getEmail(), //
						user.getName(), user.getPhone(), user.getAge(), user.getSex(), //
						user.getCity(), mapper.writeValueAsString(item), LocalDate.now());
			}
		} catch (Exception e) {
			throw e;
		}
		return new BasicRes( //
				ResCodeMessage.SUCCESS.getCode(), //
				ResCodeMessage.SUCCESS.getMessage()); //
	}

	// 私有方法檢查是否必填
	private BasicRes checkRequiredAnswer(int questionId, List<Answer> answerList, String type) {
		for (Answer answer : answerList) {
			if (answer.getQuestionId() == questionId) {
				if (type.equalsIgnoreCase(QuestionType.SINGLE.getType())) {
					// 單選
					if (answer.getRadioAnswer() <= 0) {
						return new BasicRes(ResCodeMessage.RADIO_IS_NEED.getCode(), "題目 " + questionId + " 的單選題未填寫");
					}
				} else if (type.equalsIgnoreCase(QuestionType.TEXT.getType())) {
					// 文字題
					if (!StringUtils.hasText(answer.getTextAnswer())) {
						return new BasicRes(ResCodeMessage.TEXT_IS_NEED.getCode(), "題目 " + questionId + " 的文字題未填寫");
					}
				} else {
					// 多選題 - 修正這段邏輯
					boolean hasChecked = false;
					for (Options item : answer.getOptionsList()) {
						if (item.isBoxBoolean()) {
							hasChecked = true;
							break; // ✅ 找到就跳出
						}
					}
					if (!hasChecked) {
						return new BasicRes(ResCodeMessage.CHECKBOX_IS_NEED.getCode(), "題目 " + questionId + " 的多選題未填寫");
					}
				}
			}
		}
		return null;
	}

	// 私有方法 轉換選項內容，檢查內容是否相同
	private BasicRes checkOptions(String optionsStr, List<Options> reqOptionsList) throws Exception {
		// 轉換optionsStr變成List<Options>
		try {
			List<Options> optionsList = mapper.readValue(optionsStr, new TypeReference<>() {
			});
			for (Options item : optionsList) {
				int code = item.getCode();
				String optionName = item.getOptionName();
				for (Options reqItem : reqOptionsList) {
					// 相同編號下，選項不同則回傳錯誤
					if (code == reqItem.getCode()) {
						if (!optionName.equalsIgnoreCase(reqItem.getOptionName())) {
							return new BasicRes( //
									ResCodeMessage.QUESTION_OPTION_MISMATCH.getCode(), //
									ResCodeMessage.QUESTION_OPTION_MISMATCH.getMessage());
						}
					}
				}

			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	// 取得一個問卷的fillin
	public FeedbackRes feedback(int quizId) throws Exception {
		if (quizId <= 0) {
			return new FeedbackRes(ResCodeMessage.QUIZ_ID_ERROR.getCode(), //
					ResCodeMessage.QUIZ_ID_ERROR.getMessage());
		}
		List<Question> questionList = questionDao.getByQuizId(quizId);
		// 將 Question 轉成 QuestionAnswerVo
		Map<Integer, QuestionAnswerVo> map = new HashMap<>();
		for (Question question : questionList) {
			QuestionAnswerVo vo = new QuestionAnswerVo(question.getQuizId(), //
					question.getQuestionId(), question.getName(), //
					question.getType(), question.isNeed(), question.isExist());
			// 問題編號，vo
			map.put(question.getQuestionId(), vo);
		}
		Quiz quiz = quizDao.getQuizById(quizId);

		List<Fillin> fillinList = fillinDao.getByQuizId(quizId);
		// ====================================
		// 一個 email 表示一位使用者的 FeedbackVo
		Map<String, FeedbackVo> emailFeedbackVoMap = new HashMap<>();
		for (Fillin item : fillinList) {
			FeedbackVo feedbackVo = new FeedbackVo();
			List<QuestionAnswerVo> questionAnswerVoList = new ArrayList<>();
			String email = item.getEmail();
			if (!emailFeedbackVoMap.containsKey(email)) { // 表示尚未記錄到該 user 的答案
				User user = new User(item.getName(), item.getPhone(), item.getEmail(), //
						item.getAge(), item.getSex(), item.getCity());
				// 將 User、Quiz、QuestionVoList、FillinDate 設定到 feedbackVo
				feedbackVo.setUser(user);
				feedbackVo.setQuiz(quiz);
				feedbackVo.setFillinDate(item.getFillinDate());
				feedbackVo.setQuestionVoList(questionAnswerVoList);
				emailFeedbackVoMap.put(email, feedbackVo);
			} else {
				feedbackVo = emailFeedbackVoMap.get(email);
				questionAnswerVoList = feedbackVo.getQuestionVoList();
			}

			try {
				Answer ans = mapper.readValue(item.getAnswerStr(), Answer.class);
				// 透過 questionId 當作 key 從 map 中取得對應的 QuestionAnswerVo
				QuestionAnswerVo vo = map.get(item.getQuestionId());
				vo.setOptionsList(ans.getOptionsList());
				vo.setTextAnswer(ans.getTextAnswer());
				vo.setRadioAnswer(ans.getRadioAnswer());
				questionAnswerVoList.add(vo);
			} catch (Exception e) {
				throw e;
			}

		}
		// 將 emailFeedbackVoMap 的 FeedbackVo 增加到 feedbackVoList
		List<FeedbackVo> feedbackVoList = new ArrayList<>();
		for (Entry<String, FeedbackVo> mapItem : emailFeedbackVoMap.entrySet()) {
			feedbackVoList.add(mapItem.getValue());
		}
		return new FeedbackRes(ResCodeMessage.SUCCESS.getCode(), //
				ResCodeMessage.SUCCESS.getMessage(), feedbackVoList);
	}

	// ---------------------------- 統計圖表------------------------------
	public StatisticRes statiststic(int quizId) throws Exception {
		if (quizId <= 0) {
			return new StatisticRes(ResCodeMessage.QUIZ_ID_ERROR.getCode(), //
					ResCodeMessage.QUIZ_ID_ERROR.getMessage());
		}

		// 將 問題資訊 透過 quizId 撈出來設定給 QuestionCountVo
		List<Question> questionList = questionDao.getByQuizId(quizId);
		// 將 Question 轉成 QuestionAnswerVo
		Map<Integer, QuestionCountVo> map = new HashMap<>();
		for (Question question : questionList) {
			// 跳過簡答題
			if (QuestionType.checkTextType(question.getType())) {
				continue;
			}

			QuestionCountVo vo = new QuestionCountVo( //
					question.getQuestionId(), question.getName(), //
					question.getType(), question.isNeed());
			// 問題編號，vo
			map.put(question.getQuestionId(), vo);
		}

		// 問題編號 (問題1)，List<OptionsCount> (問題1的所有選項統計結果)
		Map<Integer, List<OptionsCount>> questionIdMap = new HashMap<>();
		// 一張表 存放 某一題 使用者選擇的 每筆資料(代碼；名稱；次數)
		List<OptionsCount> opCountList = new ArrayList<>();
		// 撈一張問卷全部人的答案
		List<Fillin> fillinList = fillinDao.getByQuizId(quizId);

		// 跑fillinList迴圈，蒐集每個選擇題 之 每個選項 之 被選取的次數，存到opCountList
		for (Fillin item : fillinList) {

			try {
				// 將(item)answer_str轉成(Answer)ans物件
				Answer ans = mapper.readValue(item.getAnswerStr(), Answer.class);
				// 如果文字題，跳過
				if (StringUtils.hasText(ans.getTextAnswer())) {
					continue;
				}
				// 如果 questionIdmap 已經有這題的資料（表示之前有人回答過這題）
				if (questionIdMap.containsKey(item.getQuestionId())) {
					// map.get 取出這一題目前已經累積的統計結果 1: [ (1, "蘋果"), (2, "香蕉") ]
					// 累積的統計結果 放到 opCountList
					opCountList = questionIdMap.get(item.getQuestionId());
				} else {
					// 不同題目，就開新的表，存放統計答案
					opCountList = new ArrayList<>();
				}

				// 如果單選題
				if (ans.getRadioAnswer() > 0) {
					// optionName空字串，晚點存放選項名稱
					String optionName = "";
					// 配對op(選項代碼) = ans(使用者選擇代碼)，optionName = 選項名稱
					for (Options op : ans.getOptionsList()) {
						if (op.getCode() == ans.getRadioAnswer()) {
							optionName = op.getOptionName();
						}
					}
					// opCount得到使用者有選的 選項代碼、選項名稱、次數(默認1次)
					OptionsCount opCount = new OptionsCount(ans.getRadioAnswer(), optionName);
					// 將每筆使用者選擇的資料存到List
					opCountList.add(opCount);
				} else { // 如果多選題
					for (Options op : ans.getOptionsList()) {
						// BoxBoolean = true (被選擇時)
						if (op.isBoxBoolean()) {
							OptionsCount opCount = new OptionsCount(op.getCode(), //
									op.getOptionName());
							opCountList.add(opCount);
							// add的動作 opCountList = [ (1, "蘋果"), (2, "香蕉"), (1, "蘋果") ]
						}
					}
				}
				// 把opCountList = [ (1, "蘋果"), (2, "香蕉"), (1, "蘋果") ] 放回map
				// key 1
				// value [ (1, "蘋果"), (2, "香蕉"), (1, "蘋果") ]
				questionIdMap.put(item.getQuestionId(), opCountList);
			} catch (Exception e) {
				throw e;
			}
		}

		// 統計選項跟次數
		// 迴圈跑 questionIdMap，每個 mapItem 代表一題的統計資料
		// 使用 entrySet() 可以同時取得 key(題目編號) 和 value(該題的選項統計清單)
		for (Entry<Integer, List<OptionsCount>> mapItem : questionIdMap.entrySet()) {
			// 取得題目編號
			int questionId = mapItem.getKey();
			// 一個問卷 中 所有題目 的 value
			List<OptionsCount> value = mapItem.getValue();
			// opCodeCountMap 存放 該題 所有的 <選項編號，答案(code，選項名稱，次數)>
			Map<Integer, OptionsCount> opCodeCountMap = new HashMap<>();
			// 迴圈某一題(key固定)的value(OptionsCount)
			for (OptionsCount item : value) {
				// 若選項編號(code)已存在於map(opCodeCountMap)中
				if (opCodeCountMap.containsKey(item.getCode())) {
					// 取出舊的資料，次數 +1
					OptionsCount opc = opCodeCountMap.get(item.getCode());
					// 新資料放回opc
					opc.setCount(opc.getCount() + 1);
					opCodeCountMap.put(item.getCode(), opc);
				} else {
					// 如果code還沒存在，預設為第1次
					opCodeCountMap.put(item.getCode(), item);
				}
			}

			// 次數放回到QuestionCountVo
			// 將統計好的 opCodeCountMap 轉成 List，設定到對應的 QuestionCountVo
			if (map.containsKey(questionId)) {
				QuestionCountVo vo = map.get(questionId);
				// 將 Map 的 values (統計好的結果) 轉成 List
				List<OptionsCount> statisticsList = new ArrayList<>(opCodeCountMap.values());
				// 將統計好的 List<OptionsCount> 設到 QuestionCountVo 裡
				vo.setOptionsCountList(statisticsList);
			}
		}

		// 統計選項跟次數後，建立 StatisticRes 回傳
		Quiz quiz = quizDao.getQuizById(quizId);

		// 建立 StatisticVo，把 quiz 與 QuestionCountVo list 包進去
		StatisticVo statisticVo = new StatisticVo();
		statisticVo.setQuiz(quiz);
		statisticVo.setQuestionCountVoList(new ArrayList<>(map.values()));

		// 建立 StatisticRes
		StatisticRes res = new StatisticRes();
		res.setCode(ResCodeMessage.SUCCESS.getCode());
		res.setMessage(ResCodeMessage.SUCCESS.getMessage());
		res.setStatisticVo(statisticVo);

		return res;
	}

	// ------------------------老師統計更新版---------------------
	public StatisticRes statisticNew(int quizId) throws Exception {
		if (quizId <= 0) {
			return new StatisticRes(ResCodeMessage.QUIZ_ID_ERROR.getCode(), //
					ResCodeMessage.QUIZ_ID_ERROR.getMessage());
		}
		// 將問題相關的資訊設定給 QuestionAnswerVo
		// questionId, QuestionCountVo
		Map<Integer, QuestionCountVo> voMap = setQuestionAnswerVo(quizId);
		// 使用 QuizId 撈取所有的填答
		List<Fillin> fillinList = fillinDao.getByQuizId(quizId);
		// 問題編號, 選項編號 選項 次數
		Map<Integer, Map<Integer, Map<String, Integer>>> map = new HashMap<>();
		for (Fillin fillin : fillinList) {
			try {
				// 1. 把 answer_str 轉成 Answer
				Answer ans = mapper.readValue(fillin.getAnswerStr(), Answer.class);
				// 2. 統計次數
				// 2.1 簡答題
				if (StringUtils.hasText(ans.getTextAnswer())) {
					// textAnswer 有內容的話，表示該題是簡答題 --> 跳過
					continue;
				}
				// 選項編號, 選項, 次數
				Map<Integer, Map<String, Integer>> codeOpCountMap = new HashMap<>();
				if (map.containsKey(ans.getQuestionId())) {
					// 若問題編號已存在，則把對應的 選項編號、選項、次數的 Map 取出
					codeOpCountMap = map.get(ans.getQuestionId());
				}
				// 2.2 多選題: 先做的原因是因為要先取得選項編號與選項，而其答案是綁定在 List<Options> 中
				// 可以順便蒐集次數
				for (Options op : ans.getOptionsList()) {
					// 先判斷 opCountMap 中是否已有蒐集過的選項編號
					if (codeOpCountMap.containsKey(op.getCode())) {
						// 有蒐集過的選項編號
						// 判斷 checkBoolean 的值是否為 true
						if (op.isBoxBoolean()) {
							// --> 取出對應的 value (選項和次數的 map)
							// 選項, 次數
							Map<String, Integer> opCountMap = codeOpCountMap.get(op.getCode());
							// --> 取出選項對應的次數後再 + 1
							int count = opCountMap.get(op.getOptionName()) + 1;
							// --> 將更新後的次數放回(put) opCountMap
							opCountMap.put(op.getOptionName(), count);
							// codeOpCountMap 不需要更新，因為其對應 value 的記憶體上的值(opCountMap)已更新
						}
					} else {
						// 沒有蒐集過的選項編號 --> 建立新的, 次數是 0
						Map<String, Integer> opCountMap = new HashMap<>();
						int count = 0;
						// checkBoolean 的值是否為 true
						if (op.isBoxBoolean()) {
							// 有的話 --> 次數變成 1
							count = 1;
						}
						opCountMap.put(op.getOptionName(), count);
						// 將結果更新回 codeOpCountMap
						codeOpCountMap.put(ans.getQuestionId(), opCountMap);
					}
				}
				// 至此選項編號和選項已蒐集完畢
				// 2.3 單選題
				if (ans.getRadioAnswer() > 0) {
					// 根據選項編號從 Map<選項編號,Map<選項,次數>> 中取出對應的 Map<選項,次數>
					Map<String, Integer> opCountMap = codeOpCountMap.get(ans.getRadioAnswer());
					// 更新次數
					for (String optionName : opCountMap.keySet()) {
						// opCountMap 中只會有一筆資料而已，因為一個選項編號下，只會有一個選項和一個次數
						int count = opCountMap.get(optionName) + 1;
						opCountMap.put(optionName, count);
					}
					map.put(ans.getQuestionId(), codeOpCountMap);
				}
			} catch (Exception e) {
				throw e;
			}
		}
		// 將每一題中每個編號的選項和次數設定回 QuestionCountVo
		List<QuestionCountVo> voList = setAndGetQuestionCountVoList(map, voMap);
		Quiz quiz = quizDao.getQuizById(quizId);
		StatisticVo statisticVo = new StatisticVo(quiz, voList);
		return new StatisticRes( //
				ResCodeMessage.SUCCESS.getCode(), //
				ResCodeMessage.SUCCESS.getMessage(), statisticVo);
	}

	private Map<Integer, QuestionCountVo> setQuestionAnswerVo(int quizId) {
		// 將問題相關的資訊設定給 QuestionAnswerVo
		List<Question> questionList = questionDao.getByQuizId(quizId);
		// 將 Question 轉成 QuestionAnswerVo
		Map<Integer, QuestionCountVo> map = new HashMap<>();
		for (Question question : questionList) {
			// 跳過簡答題
			if (QuestionType.checkTextType(question.getType())) {
				continue;
			}
			QuestionCountVo vo = new QuestionCountVo(//
					question.getQuestionId(), question.getName(), //
					question.getType(), question.isNeed());
			// 問題編號，vo
			map.put(question.getQuestionId(), vo);
		}
		return map;
	}

	private List<QuestionCountVo> setAndGetQuestionCountVoList(Map<Integer, Map<Integer, Map<String, Integer>>> map, //
			Map<Integer, QuestionCountVo> voMap) {
		List<QuestionCountVo> voList = new ArrayList<>();
		for (int questionId : map.keySet()) {
			List<OptionsCount> opCountList = new ArrayList<>();
			// 取出對應的 Map<選項編號, Map<選項, 次數>>
			Map<Integer, Map<String, Integer>> codeOpCountMap = map.get(questionId);
			// 以下2種寫法擇一
			// 寫法1
			for (int code : codeOpCountMap.keySet()) {
				Map<String, Integer> opNameCountMap = codeOpCountMap.get(code);
				for (String opName : opNameCountMap.keySet()) {
					int count = opNameCountMap.get(opName);
					OptionsCount opCount = new OptionsCount(code, opName, count);
					opCountList.add(opCount);
				}

			}
			// 寫法2: 以下是 Lambda 寫法: 執行效率有比上面的程式碼好
//			codeOpCountMap.forEach((code, v) -> {
//				v.forEach((opName, count) -> {
//					OptionsCount opCount = new OptionsCount(code, opName, count);
//					opCountList.add(opCount);
//				});
//			});
			// voMap 是之前先整理過的 Map<問題編號, QuestionCountVo>，所以所有選擇題都會有
			QuestionCountVo vo = voMap.get(questionId);
			vo.setOptionsCountList(opCountList);
			voList.add(vo);
		}
		return voList;
	}

}
