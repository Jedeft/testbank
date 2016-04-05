package com.ncu.testbank.student.data.params;

import java.util.List;

public class BatchStudentAnswerParams {
	// 考试
	private Long test_id;
	private List<QuestionParams> questionParams;


	public Long getTest_id() {
		return test_id;
	}

	public void setTest_id(Long test_id) {
		this.test_id = test_id;
	}

	public List<QuestionParams> getQuestionParams() {
		return questionParams;
	}

	public void setQuestionParams(List<QuestionParams> questionParams) {
		this.questionParams = questionParams;
	}

}
